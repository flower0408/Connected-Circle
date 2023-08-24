export class GroupRequest {
    _id: number;
    approved: boolean;
    createdAt: string;
    at: string;
    createdByUserId: number;
    forGroupId: number;

    constructor(obj: {
        _id?: number,
        approved?: boolean,
        createdAt?: string,
        at?: string,
        createdByUserId?: number,
        forGroupId?: number
    } = {}) {
        this._id = obj._id || null as unknown as number;
        this.approved = obj.approved || null as unknown as boolean;
        this.createdAt = obj.createdAt || null as unknown as string;
        this.at = obj.at || null as unknown as string;
        this.createdByUserId = obj.createdByUserId || null as unknown as number;
        this.forGroupId = obj.forGroupId || null as unknown as number;
    }

    get id() {
        return this._id;
    }
}
