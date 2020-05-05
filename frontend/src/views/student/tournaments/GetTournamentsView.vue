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
          <v-spacer /><v-spacer /><v-spacer /><v-spacer />
          <v-btn
            color="primary"
            dark
            @click="newTournament"
            data-cy="createTournamentButton"
            >New Tournament</v-btn
          >
        </v-card-title>
      </template>

      <template v-slot:item.action="{ item }">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon
              small
              class="mr-2"
              v-on="on"
              @click="addUser(item)"
              data-cy="joinTournament"
              >fas fa-arrow-right</v-icon
            >
          </template>
          <span>Join Tournament</span>
        </v-tooltip>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon
              small
              class="mr-2"
              v-on="on"
              @click="cancelTournament(item)"
              data-cy="cancelTournament"
              >cancel</v-icon
            >
          </template>
          <span>Cancel Tournament</span>
        </v-tooltip>
      </template>
    </v-data-table>
    <edit-tournament-dialog
      v-if="currentTournament"
      v-model="editTournamentDialog"
      :tournament="currentTournament"
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
import EditTournamentDialog from '@/views/student/tournaments/EditTournamentDialog.vue';

@Component({
  components: {
    'edit-tournament-dialog': EditTournamentDialog
  }
})
export default class GetTournamentsView extends Vue {
  topics: Topic[] = [];
  topicsAuxiliar: number | undefined;
  currentTournament: Tournament | null = null;
  editTournamentDialog: boolean = false;
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
      text: 'Topics',
      value: 'topicsAux',
      align: 'center',
      width: '20%'
    },
    {
      text: 'Number of Questions',
      value: 'numberOfQuestions',
      align: 'center',
      sortable: false,
      width: '10%'
    },
    {
      text: 'Actions',
      value: 'action',
      align: 'center',
      sortable: false,
      width: '5%'
    }
  ];

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.tournaments = await RemoteServices.getTournaments();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async newTournament() {
    this.currentTournament = new Tournament();
    this.editTournamentDialog = true;
  }

  async onCreateTournament(tournament: Tournament) {
    this.tournaments.unshift(tournament);
    this.editTournamentDialog = false;
    this.currentTournament = null;
  }

  async onCloseDialog() {
    this.editTournamentDialog = false;
    this.currentTournament = null;
  }

  async addUser(tournament: Tournament) {
    if (confirm('Are you sure you want to join this tournament?')) {
      try {
        await RemoteServices.addUser(tournament.id);
      } catch (error) {
        await this.$store.dispatch('error', error);
      }
    }
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
