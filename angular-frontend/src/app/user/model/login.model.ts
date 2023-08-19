export class Login {
    username: string;
    password: string;

    constructor(obj : {
        username?: string,
        password?: string
    } = {}) {
        this.username = obj.username || null as unknown as string;
        this.password = obj.password || null as unknown as string;
    }
}
