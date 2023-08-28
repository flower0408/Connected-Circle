export class Banned {
  _id: number;
  blocked: boolean;
  timestamp: string;
  byAminId: number;
  towardsUserId: number;

  constructor(obj: {
    _id?: number,
    timestamp?: string,
    blocked?: boolean,
    byAminId?: number,
    towardsUserId?: number,
  } = {}) {
    this._id = obj._id || null as unknown as number;
    this.timestamp = obj.timestamp || null as unknown as string;
    this.byAminId = obj.byAminId || null as unknown as number;
    this.blocked = obj.blocked || null as unknown as boolean;
    this.towardsUserId = obj.towardsUserId || null as unknown as number;
  }

  get id() {
    return this._id;
  }
}
