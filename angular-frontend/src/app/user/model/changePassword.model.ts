export class ChangePassword {
    oldPassword: string;
    newPassword: string;

    constructor(obj : {
        oldPassword?: string,
        newPassword?: string
    } = {}) {
        this.oldPassword = obj.oldPassword || null as unknown as string;
        this.newPassword = obj.newPassword || null as unknown as string;
    }
}
