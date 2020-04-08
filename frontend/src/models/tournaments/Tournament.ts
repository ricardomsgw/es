import Course from '@/models/user/Course';

export default class Tournament {
  tournamentId: number | undefined;
  courseExecutionId: number | undefined;
  status: string | undefined;
  startDate: string | undefined;
  conclusionDate: string | undefined;
  numberOfQuestions: number | undefined;
  topics: Array<number> = [];

  constructor(jsonObj?: Tournament) {
    if (jsonObj) {
      let i = 0;
      this.tournamentId = jsonObj.tournamentId;
      this.courseExecutionId = jsonObj.courseExecutionId;
      this.startDate = jsonObj.startDate;
      this.conclusionDate = jsonObj.conclusionDate;
      this.status = jsonObj.status;
      this.numberOfQuestions = jsonObj.numberOfQuestions;

      for (i; i < jsonObj.topics.length; i++) {
        this.topics.push(jsonObj.topics[i]);
      }
    }
  }
}
