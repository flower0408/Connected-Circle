export class Comment {
    _id: number;
    text: string;
    timestamp: string;
    repliesToCommentId: number;
    belongsToUserId: number;
    belongsToPostId: number;

    constructor(obj: {
        _id?: number,
        text?: string,
        timestamp?: string,
        repliesToCommentId?: number,
        belongsToUserId?: number,
        belongsToPostId?: number
    } = {}) {
        this._id = obj && obj._id || null as unknown as number;
        this.text = obj && obj.text || null as unknown as string;
        this.timestamp = obj && obj.timestamp || null as unknown as string;
        this.repliesToCommentId = obj && obj.repliesToCommentId || null as unknown as number;
        this.belongsToUserId = obj && obj.belongsToUserId || null as unknown as number;
        this.belongsToPostId = obj && obj.belongsToPostId || null as unknown as number;
    }

    get id() {
        return this._id;
    }
}
