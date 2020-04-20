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
            v-model="search"
            append-icon="search"
            label="Search"
            class="mx-2"
          />
          <v-spacer />
          <v-btn color="primary" dark @click="newTournament" data-cy="createTournamentButton"
            >New Tournament</v-btn
          >
        </v-card-title>
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
import EditTournamentDialog from '@/views/student/tournaments/EditTournamentDialog.vue';

@Component({
  components: {
    'edit-tournament-dialog': EditTournamentDialog
  }
})
export default class CreateTournamentsView extends Vue {
  tournaments: Tournament[] = [];
  currentTournament: Tournament | null = null;
  courseExecutionId: number | undefined;
  editTournamentDialog: boolean = false;
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
      value: 'topics',
      align: 'center',
      width: '10%'
    },
    {
      text: 'Number of Questions',
      value: 'numberOfQuestions',
      align: 'center',
      sortable: false,
      width: '10%'
    }
  ];

  /*async created() {
    await this.$store.dispatch('loading');
    try {
      this.tournaments = await RemoteServices.getTournaments();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }*/

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
}
</script>

<style lang="scss" scoped></style>
