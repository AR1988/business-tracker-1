import {Component, OnDestroy, OnInit} from '@angular/core';
import {MileStoneService} from '../../../serivce/mile-stone.service';
import {RoadMapService} from '../../../serivce/road-map.service';
import {KpiService} from '../../../serivce/kpi.service';
import {TaskService} from '../../../serivce/task.service';
import {MemberService} from '../../../serivce/member.service';
import {MemberToDisplay} from '../../../models/member/member-to-display';
import {RoadMapToDisplay} from '../../../models/road-map/road-map-to-display';
import {MileStoneToDisplay} from '../../../models/mile-stone/mile-stone-to-display';
import {KpiToDisplay} from '../../../models/kpi/kpi-to-display';
import {TaskToDisplay} from '../../../models/task/task-to-display';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit, OnDestroy {

  activeTasks: TaskToDisplay[];
  kpis: KpiToDisplay[];
  mileStones: MileStoneToDisplay[];
  members: MemberToDisplay[];
  roadMaps: RoadMapToDisplay[];

  private subscriptions: Subscription[] = [];

  height: '60px';

  constructor(private taskService: TaskService,
              private roadMapService: RoadMapService,
              private kpiService: KpiService,
              private mileStoneService: MileStoneService,
              private memberService: MemberService) {
  }

  ngOnInit(): void {
    const getAllRoadMapsSubscription = this.roadMapService.getAll().subscribe(value => this.roadMaps = value);
    const getAllKpisSubscription = this.kpiService.getAll().subscribe(value => this.kpis = value);
    const getAllMileStonesSubscription = this.mileStoneService.getAll().subscribe(value => this.mileStones = value);

    const getAllActiveTasks: Subscription = this.taskService.getAllByParams('true', 'active').subscribe(value => this.activeTasks = value);
    const getAllMembersSubscription = this.memberService.getAll().subscribe(value => this.members = value);

    this.subscriptions.push(getAllKpisSubscription,
      getAllRoadMapsSubscription,
      getAllMileStonesSubscription,
      getAllActiveTasks,
      getAllMembersSubscription);
  }

  numberToString(id: number): string {
    return String(id);
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      if (subscription) {
        subscription.unsubscribe();
      }
    }
  }
}
