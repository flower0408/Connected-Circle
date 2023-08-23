export class Reaction {
    _id: number;
    reactionType: string;
    timestamp: string;
    madeByUserId: number;
    onCommentId: number;
    onPostId: number;

    constructor(obj: {
        _id?: number,
        reactionType?: string,
        timestamp?: string,
        madeByUserId?: number,
        onCommentId?: number,
        onPostId?: number
    } = {}) {
        this._id = obj._id || null as unknown as number;
        this.reactionType = obj.reactionType || null as unknown as string;
        this.timestamp = obj.timestamp || null as unknown as string;
        this.madeByUserId = obj.madeByUserId || null as unknown as number;
        this.onCommentId = obj.onCommentId || null as unknown as number;
        this.onPostId = obj.onPostId || null as unknown as number;
    }

    get id() {
        return this._id;
    }
}