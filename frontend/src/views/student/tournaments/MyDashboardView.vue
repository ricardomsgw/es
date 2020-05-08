<template>
  <v-card class="table">
    <v-data-table
      :headers="headers"
      :items="tournaments"
      :search="search"
      disable-pagination
      :hide-default-footer="true"
      :mobile-breakpoint="0"
      multi-sort
    >
      <template v-slot:top>
        <v-card-title>
          <v-text-field
            data-cy="searchButton"
            v-model="search"
            append-icon="search"
            label="Search"
            class="mx-2"
          />
        </v-card-title>
      </template>
      <template v-slot:item.action="{ }">
        <v-tooltip bottom>
          <template v-slot:activator="{ }">
            <v-icon
              large
              color="primary"
              dark
              @click="newTournament"
              data-cy="scoreButton"
              >score</v-icon
            >
          </template>
          <span>Check score</span>
        </v-tooltip>
      </template>
    </v-data-table>
    <score-dialog
      v-model="scoreDialog"
      v-on:new-tournament="onCreateTournament"
      v-on:close-dialog="onCloseDialog"
    />
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Course from '@/models/user/Course';
import RemoteServices from '@/services/RemoteServices';
//import EditCourseDialog from '@/views/admin/Courses/EditCourseDialog.vue';
import Tournament from '@/models/tournaments/Tournament';
import Topic from '@/models/management/Topic';
import ScoreDialog from '@/views/student/tournaments/EditTournamentDialog.vue';
import StudentStats from '@/models/statement/StudentStats';
import StatsView from '../StatsView.vue';
import EditTournamentDialog from '@/views/student/tournaments/EditTournamentDialog.vue';
import StatsTournamentView from '@/views/student/tournaments/StatsTournamentView.vue';

@Component({
  components: {
    'score-dialog': StatsTournamentView
  }
})
export default class MyDashboardView extends Vue {
  stats: StudentStats | null = null;
  topics: Topic[] = [];
  topicsAuxiliar: number | undefined;
  currentTournament: Tournament | null = null;
  scoreDialog: boolean = false;
  tournaments: Tournament[] = [];
  tournamentsAuxiliar: Tournament[] = [];
  courseExecutionId: number | undefined;
  //editCourseDialog: boolean = false;
  search: string = '';
  headers: object = [
    {
      text: 'Start Date',
      value: 'startDate',
      align: 'center',
      width: '10%'
    },
    {
      text: 'Conclusion Date',
      value: 'conclusionDate',
      align: 'center',
      width: '10%'
    },
    {
      text: 'Status',
      value: 'status',
      align: 'center',
      width: '10%'
    },
    {
      text: 'Number of Questions',
      value: 'numberOfQuestions',
      align: 'center',
      sortable: false,
      width: '10%'
    },
    {
      text: 'My Score',
      value: 'action',
      align: 'center',
      sortable: false,
      width: '5%'
    }
  ];

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.tournaments = await RemoteServices.getJoinedTournaments();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
  async newTournament() {
    this.scoreDialog = true;
  }

  async onCreateTournament(tournament: Tournament) {
    this.tournaments.unshift(tournament);
    this.scoreDialog = false;

  }

  async onCloseDialog() {
    this.scoreDialog = false;
  }

  async cancelTournament(tournamentToCancel: Tournament) {
    if (confirm('Are you sure you want to cancel this tournament?')) {
      try {
        let userId = await RemoteServices.obtainUser();
        if (userId == tournamentToCancel.creatorId) {
          await RemoteServices.cancelTournament(tournamentToCancel.id);
          this.tournaments = this.tournaments.filter(
            tournament => tournament.id != tournamentToCancel.id
          );
        } else {
          await this.$store.dispatch(
            'error',
            'Tournaments only can be canceled by its creator'
          );
        }
      } catch (error) {
        await this.$store.dispatch('error', error);
      }
    }
  }
}
</script>

<style lang="scss" scoped></style>
