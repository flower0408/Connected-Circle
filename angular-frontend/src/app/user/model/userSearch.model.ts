export class UserSearch {
  firstName: string;
  lastName: string;

    constructor(obj : {
      firstName?: string,
      lastName?: string
    } = {}) {
        this.firstName = obj.firstName || null as unknown as string;
        this.lastName = obj.lastName || null as unknown as string;
    }
}
