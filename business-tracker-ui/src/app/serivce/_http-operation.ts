// import {Observable} from 'rxjs';
// import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
//
// export abstract class HttpOperation<T, D> {
//
//   private readonly getAllPath: string;
//
//   private readonly getByIpPath: string;
//   private readonly updatePath: string;
//   private readonly removePath: string;
//   private client: HttpClient;
//
//
//   private httpOptions = {
//     headers: new HttpHeaders({'Content-Type': 'application/json'})
//   };
//
//   protected constructor(getAllPath: string, getByIpPath: string, updateByIpPath: string, removeByIpPath: string, client: HttpClient) {
//     this.getAllPath = getAllPath;
//     this.getByIpPath = getByIpPath;
//     this.updatePath = updateByIpPath;
//     this.removePath = removeByIpPath;
//     this.client = client;
//   }
//
//   getAll(): Observable<T[]> {
//     return this.client.get<T[]>(this.getAllPath);
//   }
//
//   getAllByParams(paramValue: string, paramName: string): Observable<T[]> {
//     const params: HttpParams = new HttpParams().set(paramName, paramValue);
//     return this.client.get<T[]>(this.getByIpPath + '/by', {params, ...this.httpOptions});
//   }
//
//   getById(id: string): Observable<T> {
//     const urlGetById = `${this.getByIpPath}/${id}`;
//     return this.client.get<T>(urlGetById, this.httpOptions);
//   }
//
//   add(addBody: D | T): Observable<T> {
//     return this.client.post<T>(this.getByIpPath, addBody, this.httpOptions);
//   }
//
//   updateById(itemId: number, updateBody: D | T): Observable<T> {
//     const url = `${this.getAllPath}/${itemId}`;
//     return this.client.put<T>(url, updateBody, this.httpOptions);
//   }
//
//   removeById(id: number): Observable<T> {
//     const url = `${this.getAllPath}/${id}`;
//     return this.client.delete<T>(url, this.httpOptions);
//   }
// }
