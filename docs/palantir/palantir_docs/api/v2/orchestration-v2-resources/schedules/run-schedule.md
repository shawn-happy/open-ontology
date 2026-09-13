`POST /api/v2/orchestration/schedules/{scheduleRid}/run`

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:orchestration-write`.

**OAuth2 scopes**: `api:orchestration-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `scheduleRid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |

## Response

**ScheduleRun**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ScheduleRun` | object | 是 | 示例: `{"scheduleVersionRid":"ri.scheduler.main.schedule-version.4d1eb55f-6c13-411c-a911-5d84e08d8017","createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","scheduleRid":"ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871","createdTime":"2003-05-06T12:34:56.789Z","rid":"ri.scheduler.main.run.d2a5e9c6-298d-4788-a71d-42885d7bebb3"}` |
| `ScheduleRun.rid` | string | 是 | The RID of a schedule run<br>示例: `ri.scheduler.main.run.d2a5e9c6-298d-4788-a71d-42885d7bebb3` |
| `ScheduleRun.scheduleRid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |
| `ScheduleRun.scheduleVersionRid` | string | 是 | The RID of a schedule version |
| `ScheduleRun.createdTime` | string | 是 | The time at which the schedule run was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ScheduleRun.createdBy` | string | 否 | The Foundry user who manually invoked this schedule run. Automatic trigger runs have this field set to<br>empty.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ScheduleRun.result` | union | 否 | The result of triggering the schedule. If empty, it means the service<br>is still working on triggering the schedule. |
| `ScheduleRun.result.ignored` | object | 否 | The schedule is not running as all targets are up-to-date. |
| `ScheduleRun.result.submitted` | object | 否 | The schedule has been successfully triggered. |
| `ScheduleRun.result.submitted.buildRid` | string | 是 | The RID of a Build.<br>示例: `ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58` |
| `ScheduleRun.result.error` | object | 否 | An error occurred attempting to run the schedule. |
| `ScheduleRun.result.error.errorName` | enum | 是 | 示例: `TARGETRESOLUTIONFAILURE` |
| `ScheduleRun.result.error.description` | string | 是 | — |

```json
{
  "scheduleVersionRid": "ri.scheduler.main.schedule-version.4d1eb55f-6c13-411c-a911-5d84e08d8017",
  "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "scheduleRid": "ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871",
  "createdTime": "2003-05-06T12:34:56.789Z",
  "rid": "ri.scheduler.main.run.d2a5e9c6-298d-4788-a71d-42885d7bebb3"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `RunSchedulePermissionDenied` | Could not run the Schedule. |
