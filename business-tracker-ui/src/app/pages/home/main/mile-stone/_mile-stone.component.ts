// import {Component, OnDestroy, OnInit} from '@angular/core';
// import {ActivatedRoute, Router} from '@angular/router';
// import {MileStoneService} from '../../../../serivce/mile-stone.service';
// import {RoadMapService} from '../../../../serivce/road-map.service';
// import {ResourceService} from '../../../../serivce/resource.service';
// import {KpiService} from '../../../../serivce/kpi.service';
// import {ResourceToDisplay} from '../../../../models/resource/resource-to-display';
// import {KpiToDisplay} from '../../../../models/kpi/kpi-to-display';
// import {MileStoneToDisplay} from '../../../../models/mile-stone/mile-stone-to-display';
// import {Subscription} from 'rxjs';
//
// @Component({
//   selector: 'app-mile-stone',
//   templateUrl: './mile-stone.component.html',
//   styleUrls: ['./mile-stone.component.css']
// })
// export class MileStoneComponent implements OnInit, OnDestroy {
//
//   roadMapId: string;
//   taskId: string;
//
//   resources: ResourceToDisplay[];
//   kpis: KpiToDisplay[];
//   mileStones: MileStoneToDisplay[];
//
//   private subscriptions: Subscription[] = [];
//
//   constructor(private router: Router,
//               private route: ActivatedRoute,
//               public mileStoneService: MileStoneService,
//               private roadMapService: RoadMapService,
//               private resourceService: ResourceService,
//               public kpiService: KpiService) {
//   }
//
//   ngOnInit(): void {
//     this.taskId = this.route.snapshot.paramMap.get('taskId');
//     this.roadMapId = this.route.snapshot.paramMap.get('roadMapId');
//     const productId = +this.route.snapshot.paramMap.get('productId');
//
//     const getAllResourcesSubscription = this.resourceService.getAll()
//       .subscribe(resourceArr => this.resources = resourceArr);
//
//     this.mileStoneService.getAll()
//       .subscribe(value => this.mileStones = value);
//
//     const getAllKpisSubscription = this.kpiService.getAllByProjectId(productId)
//       .subscribe(value => this.kpis = value);
//
//     const kpiAddSubscription = this.kpiService.kpiAdded$
//       .subscribe(value => this.kpis.push(value));
//
//     const kpiDeleteSubscription = this.kpiService.kpiDeleted$
//       .subscribe(value => {
//         const removeKpiItem = this.kpis.findIndex(value1 => value1.name === value.name)
//         this.kpis.splice(removeKpiItem, 1);
//       });
//
//     const reloadResourceListOnRemoveSubscription = this.resourceService.reloadResourceListRemoved$
//       .subscribe(value => {
//         const indexToRemove = this.resources.indexOf(value);
//         this.resources.splice(indexToRemove, 1);
//       });
//
//     const reloadResourceListSubscription = this.resourceService.reloadAddedResourceList$
//       .subscribe(value => {
//         this.resources = value;
//       });
//
//     this.subscriptions.push(
//       getAllKpisSubscription,
//       kpiAddSubscription,
//       kpiDeleteSubscription,
//       getAllResourcesSubscription,
//       reloadResourceListSubscription,
//       reloadResourceListOnRemoveSubscription
//     );
//   }
//
//   ngOnDestroy(): void {
//     for (const subscription of this.subscriptions) {
//       if (subscription) {
//         subscription.unsubscribe();
//       }
//     }
//   }
// }
