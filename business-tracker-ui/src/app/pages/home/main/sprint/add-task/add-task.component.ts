import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MemberToAdd} from '../../../../../models/member/member-to-add';
import {MemberToDisplay} from '../../../../../models/member/member-to-display';
import {TaskService} from '../../../../../serivce/task.service';
import {DeliveryService} from '../../../../../serivce/delivery.service';
import {ResourceService} from '../../../../../serivce/resource.service';
import {MemberService} from '../../../../../serivce/member.service';
import {ResponsibleMembersService} from '../../../../../serivce/responsible-members.service';
import {TaskToAdd} from '../../../../../models/task/task-to-add';
import {ActivatedRoute, Router} from '@angular/router';
import {ResourceToAdd} from '../../../../../models/resource/resource-to-add';
import {Observable, Subscription} from 'rxjs';
import {DeliveryToDisplay} from "../../../../../models/delivery/delivery-to-display";


@Component({
  selector: 'app-add-task',
  templateUrl: './add-task.component.html',
  styleUrls: ['./add-task.component.css']
})
export class AddTaskComponent implements OnInit, OnDestroy {
  form: FormGroup;

  membersToSelect: MemberToDisplay;
  private resources: ResourceToAdd[] = [];

  membersList: Observable<MemberToDisplay[]>;
  subscriptions: Subscription[] = [];
  deliveries: DeliveryToDisplay[] = [
    {name: 'Документ', taskId: undefined, id: 1},
    {name: 'Презентация', taskId: undefined, id: 2}
  ];

  active = false;
  finished = false;

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private taskService: TaskService,
              private deliveryService: DeliveryService,
              private resourceService: ResourceService,
              public memberService: MemberService,
              private responsibleMembersService: ResponsibleMembersService,
              private router: Router) {
  }


  ngOnInit(): void {
    this.initForm();
    this.membersList = this.memberService.getAll();
    this.subscribeResourceTriggers();
  }

  onSubmit(): void {

    const taskToAdd: TaskToAdd = new TaskToAdd();
    taskToAdd.name = this.form.controls.name.value;
    taskToAdd.mileStoneId = +this.route.snapshot.paramMap.get('taskId');

    taskToAdd.active = this.active;
    taskToAdd.finished = this.finished;

    const addSprintSub = this.taskService.addAll(
      {
        task: taskToAdd,
        member: this.getAddMember(),
        delivery: this.form.controls.delivery.value,
        resources: this.resources ? this.resources : []
      }).subscribe(() => this.navigateToTaskPage(), error => console.error(error));

    this.subscriptions.push(addSprintSub);
  }

  navigateToTaskPage(): void {
    this.router.navigate(['../../..'], {relativeTo: this.route});
  }

  private getAddMember(): MemberToAdd {
    const member = this.form.controls.member.value;
    const selectedMember = new MemberToAdd();
    selectedMember.name = member.name;
    selectedMember.lastName = member.lastName;
    selectedMember.img = member.img;
    selectedMember.position = member.position;
    selectedMember.role = member.role;
    selectedMember.roadMapId = member.roadMapId;
    return selectedMember;
  }

  private initForm() {
    this.form = this.fb.group({
      name: ['', Validators.required],
      member: ['', Validators.required],
      delivery: ['', Validators.required]
    });
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      if (subscription) {
        subscription.unsubscribe();
      }
    }
  }

  private subscribeResourceTriggers() {
    const transferSubscribe = this.resourceService.addResourceTransfer$
      .subscribe(value => {
        this.resources.push(value);
        this.resourceService.reloadAddedResourceList$.next(this.resources);
      });

    const resRemovedSub = this.resourceService.reloadResourceListRemoved$
      .subscribe(value => {
        const removedResIndex = this.resources.indexOf(value);
        this.resources.splice(removedResIndex, 1);
      });

    this.subscriptions.push(transferSubscribe, resRemovedSub);
  }
}
