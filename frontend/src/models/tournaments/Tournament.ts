import Course from '@/models/user/Course';
import Topic from '@/models/management/Topic';

export default class Tournament {
  tournamentId: number | undefined;
  courseExecutionId: number = 11;
  status: string = 'CREATED';
  startDate: string | undefined;
  conclusionDate: string | undefined;
  numberOfQuestions: number | undefined;
  topics: number[] = [];

  constructor(jsonObj?: Tournament) {
    if (jsonObj) {
      let i = 0;
      this.tournamentId = jsonObj.tournamentId;
      this.courseExecutionId = jsonObj.courseExecutionId;
      this.startDate = jsonObj.startDate;
      this.conclusionDate = jsonObj.conclusionDate;
      this.status = jsonObj.status;
      this.numberOfQuestions = jsonObj.numberOfQuestions;
      this.topics = jsonObj.topics;
    }
  }
}
