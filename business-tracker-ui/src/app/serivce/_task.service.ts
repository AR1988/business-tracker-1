// import {Injectable} from '@angular/core';
// import {HttpClient, HttpHeaders} from '@angular/common/http';
// import {HttpOperation} from './http-operation';
// import {TaskToDisplay} from '../models/task/task-to-display';
// import {TaskToAdd} from '../models/task/task-to-add';
// import {MemberToAdd} from '../models/member/member-to-add';
// import {DeliveryToAdd} from '../models/delivery/delivery-to-add';
// import {ResourceToAdd} from '../models/resource/resource-to-add';
//
// @Injectable({
//   providedIn: 'root'
// })
// export class TaskService extends HttpOperation<TaskToDisplay, TaskToAdd> {
//
//
//   constructor(private http: HttpClient) {
//     // super('api/sprints', 'api/sprints', 'api/sprints', 'api/sprints', http);
//     super('api/task', 'api/task', 'api/task', 'api/task', http);
//   }
//
//   addAll(arr: { sprint: TaskToAdd, member: MemberToAdd, delivery: DeliveryToAdd, resources: ResourceToAdd[] }) {
//     return this.http.post<TaskToDisplay>('api/sprints', arr, {
//       headers: new HttpHeaders({'Content-Type': 'application/json'})
//     });
//   }
// }
