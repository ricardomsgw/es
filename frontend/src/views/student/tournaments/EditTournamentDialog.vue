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
              <v-text-field
                v-model="editTournament.startDate"
                label="Start Date"
                data-cy="Start Date"
              />
            </v-flex>
            <v-flex xs24 sm12 md8>
              <v-text-field
                v-model="editTournament.conclusionDate"
                label="Conclusion Date"
                data-cy="Conclusion Date"
              />
            </v-flex>
            <v-flex xs24 sm12 md8>
              <v-text-field
                v-model="editTournament.numberOfQuestions"
                label="Number Of Questions"
                data-cy="Number Of Questions"
              />
            </v-flex>
              <!-- <v-flex xs24 sm12 md8>
              <v-text-field
                v-model="editTournament.topics"
                label="Topics"
                data-cy="topics"
              />
            </v-flex>-->
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

@Component
export default class EditTournamentDialog extends Vue {
  @Model('dialog', Boolean) dialog!: boolean;
  @Prop({ type: Tournament, required: true }) readonly tournament!: Tournament;

  editTournament!: Tournament;
  //arrayAux!: number[];
  isCreateTournament: boolean = false;

  created() {
    this.editTournament = new Tournament(this.tournament);
    //this.isCreateTournament = !!this.editTournament.name;
  }

  async saveTournament() {
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
