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
            >delete</v-icon>
          </template>
          <span>Join Tournament</span>
        </v-tooltip>
      </template>
    </v-data-table>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Course from '@/models/user/Course';
import RemoteServices from '@/services/RemoteServices';
//import EditCourseDialog from '@/views/admin/Courses/EditCourseDialog.vue';
import Tournament from '@/models/tournaments/Tournament';
import Topic from '@/models/management/Topic';

@Component
export default class GetTournamentsView extends Vue {
  topics: Topic[] = [];
  topicsAuxiliar: number | undefined;
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
      this.tournamentsAuxiliar = await RemoteServices.getTournaments();
      this.topics = await RemoteServices.getTopics();
      this.topicsAuxiliar = this.tournamentsAuxiliar[0].topics[0];
      for (var i = 0; i <= this.tournamentsAuxiliar[0].topics.length; i++) {
        this.topics = this.topics.filter(
                element => element.id != this.tournamentsAuxiliar[0].topics[i]
        );
      }
      this.tournamentsAuxiliar[0].topicsAux = this.topics;
      this.tournaments = this.tournamentsAuxiliar;
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
  async addUser(tournament: Tournament) {
  }
}
</script>

<style lang="scss" scoped></style>
