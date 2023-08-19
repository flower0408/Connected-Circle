export class Register {
    username: string;
    password: string;
    email: string;
    firstName: string;
    lastName: string;

    constructor(obj : {
        username?: string,
        password?: string,
        email?: string,
        firstName?: string,
        lastName?: string
    } = {}) {
        this.username = obj.username || null as unknown as string;
        this.password = obj.password || null as unknown as string;
        this.email = obj.email || null as unknown as string;
        this.firstName = obj.firstName || null as unknown as string;
        this.lastName = obj.lastName || null as unknown as string;
    }
}
