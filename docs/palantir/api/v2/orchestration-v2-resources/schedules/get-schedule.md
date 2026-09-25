`GET /api/v2/orchestration/schedules/{scheduleRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get the Schedule with the specified rid.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:orchestration-read`.

**OAuth2 scopes**: `api:orchestration-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `scheduleRid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**Schedule**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Schedule` | object | 是 | 示例: `{"updatedTime":"2024-09-25T17:29:35.974Z","paused":false,"updatedBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","displayName":"My Daily Schedule","currentVersionRid":"ri.scheduler.main.schedule-version.4d1eb55f-6c13-411c-a911-5d84e08d8017","description":"Run all the transforms at midnight","createdTime":"2024-09-25T17:29:35.974Z","action":{"abortOnFailure":false,"forceBuild":false,"retryBackoffDuration":{"unit":"SECONDS","value":30},"retryCount":1,"fallbackBranches":[],"branchName":"master","notificationsEnabled":false,"target":{"type":"manual","targetRids":["ri.foundry.main.dataset.b737e24d-6b19-43aa-93d5-da9fc4073f6e","ri.foundry.main.dataset.d2452a94-a755-4778-8bfc-a315ab52fc43"]}},"trigger":{"type":"time","cronExpression":"0 0 * * *","timeZone":"UTC"},"rid":"ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871","scopeMode":{"type":"user"}}` |
| `Schedule.rid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |
| `Schedule.displayName` | string | 否 | 示例: `My Daily Schedule` |
| `Schedule.description` | string | 否 | 示例: `Run all the transforms at midnight` |
| `Schedule.currentVersionRid` | string | 是 | The RID of the current schedule version |
| `Schedule.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2024-09-25T17:29:35.974Z` |
| `Schedule.createdBy` | string | 是 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Schedule.updatedTime` | string | 是 | The time at which the resource was most recently updated.<br>示例: `2024-09-25T17:29:35.974Z` |
| `Schedule.updatedBy` | string | 是 | The Foundry user who last updated this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Schedule.paused` | boolean | 是 | 示例: `false` |
| `Schedule.trigger` | union | 否 | The schedule trigger. If the requesting user does not have<br>permission to see the trigger, this will be empty.<br>示例: `{"type":"time","cronExpression":"0 0 * * *","timeZone":"UTC"}` |
| `Schedule.trigger.jobSucceeded` | object | 否 | Trigger whenever a job succeeds on the dataset and on the target<br>branch. |
| `Schedule.trigger.jobSucceeded.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Schedule.trigger.jobSucceeded.branchName` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Schedule.trigger.or` | object | 否 | Trigger whenever any of the given triggers emit an event. |
| `Schedule.trigger.or.triggers` | list<Trigger> | 否 | — |
| `Schedule.trigger.or.triggers.Trigger` | union | 是 | — |
| `Schedule.trigger.newLogic` | object | 否 | Trigger whenever a new JobSpec is put on the dataset and on<br>that branch. |
| `Schedule.trigger.newLogic.branchName` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Schedule.trigger.newLogic.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Schedule.trigger.tableUpdated` | object | 否 | Trigger whenever a new transaction is committed to the<br>table on the target branch. |
| `Schedule.trigger.tableUpdated.tableRid` | string | 是 | The Resource Identifier (RID) of a Table.<br>示例: `ri.tables.main.table.beb574f7-019c-4a01-abd1-ff3c97bfaec8` |
| `Schedule.trigger.tableUpdated.branchName` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Schedule.trigger.and` | object | 否 | Trigger after all of the given triggers emit an event. |
| `Schedule.trigger.and.triggers` | list<Trigger> | 否 | — |
| `Schedule.trigger.and.triggers.Trigger` | union | 是 | — |
| `Schedule.trigger.datasetUpdated` | object | 否 | Trigger whenever a new transaction is committed to the<br>dataset on the target branch. |
| `Schedule.trigger.datasetUpdated.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Schedule.trigger.datasetUpdated.branchName` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Schedule.trigger.scheduleSucceeded` | object | 否 | Trigger whenever the specified schedule completes its action<br>successfully. |
| `Schedule.trigger.scheduleSucceeded.scheduleRid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |
| `Schedule.trigger.mediaSetUpdated` | object | 否 | Trigger whenever an update is made to a media set on the target<br>branch. For transactional media sets, this happens when a transaction<br>is committed. For non-transactional media sets, this event happens<br>eventually (but not necessary immediately) after an update. |
| `Schedule.trigger.mediaSetUpdated.mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |
| `Schedule.trigger.mediaSetUpdated.branchName` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Schedule.trigger.time` | object | 否 | Trigger on a time based schedule. |
| `Schedule.trigger.time.cronExpression` | string | 是 | A standard CRON expression with minute, hour, day, month<br>and day of week.<br>示例: `0 0 * * 1-5` |
| `Schedule.trigger.time.timeZone` | string | 是 | A string representation of a java.time.ZoneId<br>示例: `Europe/Paris` |
| `Schedule.trigger.manual` | object | 否 | Only trigger the Schedule manually. If placed in an AND or OR condition, this Trigger will be ignored. |
| `Schedule.action` | object | 是 | 示例: `{"abortOnFailure":false,"forceBuild":false,"retryBackoffDuration":{"unit":"SECONDS","value":30},"retryCount":1,"fallbackBranches":[],"branchName":"master","notificationsEnabled":false,"target":{"type":"manual","targetRids":["ri.foundry.main.dataset.b737e24d-6b19-43aa-93d5-da9fc4073f6e","ri.foundry.main.dataset.d2452a94-a755-4778-8bfc-a315ab52fc43"]}}` |
| `Schedule.action.target` | union | 是 | The targets of the build.<br>示例: `{"type":"manual","targetRids":["ri.foundry.main.dataset.b737e24d-6b19-43aa-93d5-da9fc4073f6e","ri.foundry.main.dataset.d2452a94-a755-4778-8bfc-a315ab52fc43"]}` |
| `Schedule.action.target.upstream` | object | 否 | Target the specified datasets along with all upstream datasets except the ignored datasets. |
| `Schedule.action.target.upstream.targetRids` | list<BuildableRid> | 否 | The target datasets. |
| `Schedule.action.target.upstream.targetRids.BuildableRid` | string | 是 | The Resource Identifier (RID) of a Resource that can be built. For example, this is a Dataset RID, Media Set<br>RID or Restricted View RID. |
| `Schedule.action.target.upstream.ignoredRids` | list<BuildableRid> | 否 | The datasets to ignore when calculating the final set of dataset to build. |
| `Schedule.action.target.upstream.ignoredRids.BuildableRid` | string | 是 | The Resource Identifier (RID) of a Resource that can be built. For example, this is a Dataset RID, Media Set<br>RID or Restricted View RID. |
| `Schedule.action.target.manual` | object | 否 | Manually specify all datasets to build. |
| `Schedule.action.target.manual.targetRids` | list<BuildableRid> | 否 | — |
| `Schedule.action.target.manual.targetRids.BuildableRid` | string | 是 | The Resource Identifier (RID) of a Resource that can be built. For example, this is a Dataset RID, Media Set<br>RID or Restricted View RID. |
| `Schedule.action.target.connecting` | object | 否 | All datasets between the input datasets (exclusive) and the<br>target datasets (inclusive) except for the datasets to ignore. |
| `Schedule.action.target.connecting.inputRids` | list<BuildableRid> | 否 | The upstream input datasets (exclusive). |
| `Schedule.action.target.connecting.inputRids.BuildableRid` | string | 是 | The Resource Identifier (RID) of a Resource that can be built. For example, this is a Dataset RID, Media Set<br>RID or Restricted View RID. |
| `Schedule.action.target.connecting.targetRids` | list<BuildableRid> | 否 | The downstream target datasets (inclusive). |
| `Schedule.action.target.connecting.targetRids.BuildableRid` | string | 是 | The Resource Identifier (RID) of a Resource that can be built. For example, this is a Dataset RID, Media Set<br>RID or Restricted View RID. |
| `Schedule.action.target.connecting.ignoredRids` | list<BuildableRid> | 否 | The datasets between the input datasets and target datasets to exclude. |
| `Schedule.action.target.connecting.ignoredRids.BuildableRid` | string | 是 | The Resource Identifier (RID) of a Resource that can be built. For example, this is a Dataset RID, Media Set<br>RID or Restricted View RID. |
| `Schedule.action.branchName` | string | 是 | The target branch the schedule should run on.<br>示例: `master` |
| `Schedule.action.fallbackBranches` | list<BranchName> | 否 | The branches to retrieve JobSpecs from if no JobSpec is found on the<br>target branch.<br>示例: `[]` |
| `Schedule.action.fallbackBranches.BranchName` | string | 是 | The name of a Branch. |
| `Schedule.action.forceBuild` | boolean | 是 | Whether to ignore staleness information when running the build.<br>示例: `false` |
| `Schedule.action.retryCount` | integer | 否 | The number of retry attempts for failed Jobs within the Build. A Job's failure is not considered final until<br>all retries have been attempted or an error occurs indicating that retries cannot be performed. Be aware,<br>not all types of failures can be retried.<br>示例: `1` |
| `Schedule.action.retryBackoffDuration` | object | 否 | The duration to wait before retrying after a Job fails.<br>示例: `{"unit":"SECONDS","value":30}` |
| `Schedule.action.retryBackoffDuration.value` | integer | 是 | The duration value.<br>示例: `30` |
| `Schedule.action.retryBackoffDuration.unit` | enum | 是 | The unit of duration.<br>示例: `SECONDS` |
| `Schedule.action.abortOnFailure` | boolean | 是 | If any job in the build is unsuccessful, immediately finish the<br>build by cancelling all other jobs.<br>示例: `false` |
| `Schedule.action.notificationsEnabled` | boolean | 是 | Whether to receive a notification at the end of the build.<br>The notification will be sent to the user that has most recently edited the schedule.<br>No notification will be sent if the schedule has `scopeMode` set to `ProjectScope`.<br>示例: `false` |
| `Schedule.scopeMode` | union | 是 | The boundaries for the schedule build.<br>示例: `{"type":"user"}` |
| `Schedule.scopeMode.project` | object | 否 | The schedule will only build resources in the following projects. |
| `Schedule.scopeMode.project.projectRids` | list<ProjectRid> | 否 | — |
| `Schedule.scopeMode.project.projectRids.ProjectRid` | string | 是 | The unique resource identifier (RID) of a Project. |
| `Schedule.scopeMode.user` | object | 否 | When triggered, the schedule will build all resources that the<br>associated user is permitted to build. |

```json
{
  "updatedTime": "2024-09-25T17:29:35.974Z",
  "paused": false,
  "updatedBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "displayName": "My Daily Schedule",
  "currentVersionRid": "ri.scheduler.main.schedule-version.4d1eb55f-6c13-411c-a911-5d84e08d8017",
  "description": "Run all the transforms at midnight",
  "createdTime": "2024-09-25T17:29:35.974Z",
  "action": {
    "abortOnFailure": false,
    "forceBuild": false,
    "retryBackoffDuration": {
      "unit": "SECONDS",
      "value": 30
    },
    "retryCount": 1,
    "fallbackBranches": [],
    "branchName": "master",
    "notificationsEnabled": false,
    "target": {
      "type": "manual",
      "targetRids": [
        "ri.foundry.main.dataset.b737e24d-6b19-43aa-93d5-da9fc4073f6e",
        "ri.foundry.main.dataset.d2452a94-a755-4778-8bfc-a315ab52fc43"
      ]
    }
  },
  "trigger": {
    "type": "time",
    "cronExpression": "0 0 * * *",
    "timeZone": "UTC"
  },
  "rid": "ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871",
  "scopeMode": {
    "type": "user"
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ScheduleNotFound` | The given Schedule could not be found. |
