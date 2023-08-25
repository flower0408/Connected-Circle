import {ReportReason} from "./reportReason";


export class Report {
  _id: number;
  reason: ReportReason;
  timestamp: string;
  byUserId: number;
  accepted: boolean;
  onUserId: number;
  onPostId: number;
  onCommentId: number;


  constructor(obj: {
    _id?: number,
    reason?: ReportReason,
    timestamp?: string,
    accepted?: boolean,
    byUserId?: number,
    onUserId?: number,
    onPostId?: number,
    onCommentId?: number
  } = {}) {
    this._id = obj._id || null as unknown as number;
    this.reason = obj.reason || null as unknown as ReportReason;
    this.timestamp = obj.timestamp || null as unknown as string;
    this.byUserId = obj.byUserId || null as unknown as number;
    this.accepted = obj.accepted || null as unknown as boolean;
    this.onUserId = obj.onUserId || null as unknown as number;
    this.onPostId = obj.onPostId || null as unknown as number;
    this.onCommentId = obj.onCommentId || null as unknown as number;
  }

  get id() {
    return this._id;
  }
}
