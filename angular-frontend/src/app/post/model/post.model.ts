import { User } from "src/app/user/model/user.model";

export class Post {
  _id: number;
  content: String;
  creationDate: Date;
  postedBy: User;

  constructor(obj?: any) {
    this._id = obj && obj._id || null;
    this.content = obj && obj.content || null;
    this.creationDate = obj && obj.creationDate || null;
    this.postedBy = obj && obj.postedBy || null;
  }
}
