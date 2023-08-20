export class Image {
    _id: number;
    path: string;
    belongsToPostId: number;
    belongsToUserId: number;

    constructor(obj: {
        _id?: number,
        path?: string,
        belongsToPostId?: number,
        belongsToUserId?: number
    } = {}) {
        this._id = obj._id || null as unknown as number;
        this.path = obj.path || null as unknown as string;
        this.belongsToPostId = obj.belongsToPostId || null as unknown as number;
        this.belongsToUserId = obj.belongsToUserId || null as unknown as number;
    }

    get id() {
        return this._id;
    }
}
