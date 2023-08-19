export class UserToken {
    accessToken: string;
    expiresIn: number;

    constructor(obj : {
        accessToken?: string,
        expiresIn?: number
    } = {}) {
        this.accessToken = obj.accessToken || null as unknown as string;
        this.expiresIn = obj.expiresIn || null as unknown as number;
    }
}
