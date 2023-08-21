export class FriendRequest {
    _id: number;
    approved: boolean;
    createdAt: string;
    at: string;
    fromUserId: number;
    toUserId: number;

    constructor(obj: {
        _id?: number,
        approved?: boolean,
        createdAt?: string,
        at?: string,
        fromUserId?: number,
        toUserId?: number
    } = {}) {
        this._id = obj._id || null as unknown as number;
        this.approved = obj.approved || null as unknown as boolean;
        this.createdAt = obj.createdAt || null as unknown as string;
        this.at = obj.at || null as unknown as string;
        this.fromUserId = obj.fromUserId || null as unknown as number;
        this.toUserId = obj.toUserId || null as unknown as number;
    }

    get id() {
        return this._id;
    }
}
