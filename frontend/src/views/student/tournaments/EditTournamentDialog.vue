<template>
  <v-dialog
    :value="dialog"
    @input="$emit('close-dialog')"
    @keydown.esc="$emit('close-dialog')"
    max-width="75%"
    max-height="80%"
  >
    <v-card>
      <v-card-title>
        <span class="headline">
          New Tournament
        </span>
      </v-card-title>

      <v-card-text class="text-left" v-if="editTournament">
        <v-container grid-list-md fluid>
          <v-layout column wrap>
            <v-flex xs24 sm12 md8>
              <v-datetime-picker
                data-cy="StartDate"
                label="Start Date"
                format="yyyy-MM-dd HH:mm"
                date-format="yyyy-MM-dd"
                time-format="HH:mm"
                v-model="editTournament.startDate"
              >
              </v-datetime-picker>
            </v-flex>
            <v-flex xs24 sm12 md8>
              <v-datetime-picker
                data-cy="ConclusionDate"
                label="Conclusion Date"
                format="yyyy-MM-dd HH:mm"
                date-format="yyyy-MM-dd"
                time-format="HH:mm"
                return-object
                v-model="editTournament.conclusionDate"
              >
              </v-datetime-picker>
            </v-flex>
            <v-flex xs24 sm12 md8>
              <v-text-field
                v-model="editTournament.numberOfQuestions"
                label="Number Of Questions"
                data-cy="NumberOfQuestions"
              />
            </v-flex>
            <v-form>
              <v-autocomplete
                :items="topics"
                multiple
                item-text="name"
                item-value="id"
                data-cy="Topics"
                return-object
                v-model="selectTopic"
              >
                <template v-slot:selection="data">
                  <v-chip
                    v-bind="data.attrs"
                    :input-value="data.selected"
                    close
                    @click="data.select"
                  >
                    {{ data.item.name }}
                  </v-chip>
                </template>
                <template v-slot:item="data">
                  <v-list-item-content>
                    <v-list-item-title v-html="data.item.name" />
                  </v-list-item-content>
                </template>
              </v-autocomplete>
            </v-form>
          </v-layout>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          color="blue darken-1"
          @click="$emit('close-dialog')"
          data-cy="cancelButton"
          >Cancel</v-btn
        >
        <v-btn
          color="blue darken-1"
          @click="saveTournament"
          data-cy="saveButton"
          >Save</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Model, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import Course from '@/models/user/Course';
import Tournament from '@/models/tournaments/Tournament';
import Topic from '@/models/management/Topic';
import moment from 'moment';
import { Quiz } from '@/models/management/Quiz';

@Component
export default class EditTournamentDialog extends Vue {
  @Model('dialog', Boolean) dialog!: boolean;
  //@Prop({ type: Array, required: true }) readonly topics!: Topic[];
  @Prop({ type: Tournament, required: true }) readonly tournament!: Tournament;
  //@Prop(Tournament) readonly editTournament!: Tournament;

  editTournament: Tournament = new Tournament(this.tournament);
  topics: Topic[] = [];
  selectTopic: Topic[] = [];
  startDate: Date | undefined;
  conclusionDate: Date | undefined;
  //arrayAux!: number[];
  isCreateTournament: boolean = false;
  startDateAux: string | undefined;
  conclusionDateAux: string | undefined;

  async created() {
    await this.$store.dispatch('loading');
    try {
      //this.editTournament = new Tournament(this.tournament);
      this.topics = await RemoteServices.getTopics();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async saveTournament() {
    this.startDateAux = moment(String(this.editTournament.startDate)).format(
      'YYYY-MM-DD hh:mm'
    );
    this.editTournament.startDate = this.startDateAux;
    this.conclusionDateAux = moment(
      String(this.editTournament.conclusionDate)
    ).format('YYYY-MM-DD hh:mm');
    this.editTournament.conclusionDate = this.conclusionDateAux;
    if (
      this.editTournament &&
      (!this.editTournament.startDate ||
        !this.editTournament.conclusionDate ||
        !this.editTournament.numberOfQuestions)
    ) {
      await this.$store.dispatch(
        'error',
        'Tournament must have a start and conclusion date, a number of questions and topics'
      );
      return;
    }

    if (this.editTournament) {
      try {
        var i = 0;
        for (i; i < this.selectTopic.length; i++) {
          this.editTournament.topics.push(this.selectTopic[i].id);
        }
        const result = await RemoteServices.createTournament(
          this.editTournament
        );
        this.$emit('new-tournament', result);
      } catch (error) {
        await this.$store.dispatch('error', error);
      }
    }
  }
}
</script>
