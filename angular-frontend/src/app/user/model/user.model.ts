export class User {
  _id: number;
  username: String;
  password: String;
  email: String;
  lastLogin: Date;
  firstName: String;
  lastName: String;

  constructor(obj?: any) {
    this._id = obj && obj._id || null;
    this.username = obj && obj.username || null;
    this.password = obj && obj.password || null;
    this.email = obj && obj.email || null;
    this.lastLogin = obj && obj.lastLogin || null;
    this.firstName = obj && obj.firstName || null;
    this.lastName = obj && obj.lastName || null;
  }
}
