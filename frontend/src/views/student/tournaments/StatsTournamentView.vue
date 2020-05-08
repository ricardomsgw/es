<template>
  <v-dialog
    :value="dialog"
    @input="$emit('close-dialog')"
    @keydown.esc="$emit('close-dialog')"
    max-width="40%"
    max-height="80%"
  >
    <v-card>
      <v-card-title>
        <span class="headline">
          My score
        </span>
      </v-card-title>

      <v-card-text class="text-left" v-if="editTournament">
        <v-container grid-list-md fluid>
          <v-layout column wrap>
            <div class="items">
              <div class="icon-wrapper" ref="correctAnswers">
                <animated-number :number="stats.correctAnswers"
                  >%</animated-number
                >
              </div>
              <div class="project-name">
                <p>Total Correct Answers</p>
              </div>
            </div>
          </v-layout>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          color="blue darken-1"
          @click="$emit('close-dialog')"
          data-cy="cancelButton"
          >Ok</v-btn
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
import Store from '@/store';
import StudentStats from '@/models/statement/StudentStats';
import AnimatedNumber from '@/components/AnimatedNumber.vue';

@Component({
  components: { AnimatedNumber }
})
export default class StatsTournamentView extends Vue {
  @Model('dialog', Boolean) dialog!: boolean;
  @Prop({ type: Tournament, required: true }) readonly tournament!: Tournament;
  stats: StudentStats | null = null;
  editTournament: Tournament = new Tournament(this.tournament);


  async created() {
    await this.$store.dispatch('loading');
    try {
      this.stats = await RemoteServices.getUserStats();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

}
</script>

<style lang="scss" scoped>
.stats-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  align-items: stretch;
  align-content: center;
  height: 100%;

  .items {
    background-color: rgba(255, 255, 255, 0.75);
    color: #1976d2;
    border-radius: 5px;
    flex-basis: 25%;
    margin: 20px;
    cursor: pointer;
    transition: all 0.6s;
  }
}

.icon-wrapper,
.project-name {
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-wrapper {
  font-size: 60px;
  transform: translateY(0px);
  transition: all 0.6s;
}

.icon-wrapper {
  align-self: end;
}

.project-name {
  align-self: self-start;
}
.project-name p {
  font-size: 24px;
  font-weight: bold;
  letter-spacing: 2px;
  transform: translateY(70px);
}

.headline {
  font-size: 24px;
  font-weight: bold;
  letter-spacing: 2px;
}
</style>
