import { Image } from "./image.model";

export class Post {
  _id: number;
  title: string;
  content: string;
  creationDate: string;
  postedByUserId: number;
  images: Image[] = [];
  belongsToGroupId: number;


  constructor(obj: {
    _id?: number,
    content?: string,
    title?:string,
    creationDate?: string,
    postedByUserId?: number,
    images?: Image[],
    belongsToGroupId?: number
  } = {}) {
    this._id = obj._id || null as unknown as number;
    this.content = obj.content || null as unknown as string;
    this.title = obj.title || null as unknown as string;
    this.creationDate = obj.creationDate || null as unknown as string;
    this.postedByUserId = obj.postedByUserId || null as unknown as number;
    this.images = obj.images || [];
    this.belongsToGroupId = obj.belongsToGroupId || null as unknown as number;
  }

  get id() {
    return this._id;
  }
}
