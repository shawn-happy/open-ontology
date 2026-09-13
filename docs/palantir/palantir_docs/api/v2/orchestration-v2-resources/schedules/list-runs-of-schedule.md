`GET /api/v2/orchestration/schedules/{scheduleRid}/runs`

Get the most recent runs of a Schedule. If no page size is provided, a page size of 100 will be used.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:orchestration-read`.

**OAuth2 scopes**: `api:orchestration-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `scheduleRid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListRunsOfScheduleResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListRunsOfScheduleResponse` | object | 是 | 示例: `{"data":[{"scheduleVersionRid":"ri.scheduler.main.schedule-version.4d1eb55f-6c13-411c-a911-5d84e08d8017","createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","scheduleRid":"ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871","createdTime":"2003-05-06T12:34:56.789Z","rid":"ri.scheduler.main.run.d2a5e9c6-298d-4788-a71d-42885d7bebb3"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListRunsOfScheduleResponse.data` | list<ScheduleRun> | 否 | — |
| `ListRunsOfScheduleResponse.data.ScheduleRun` | object | 是 | — |
| `ListRunsOfScheduleResponse.data.ScheduleRun.rid` | string | 是 | The RID of a schedule run<br>示例: `ri.scheduler.main.run.d2a5e9c6-298d-4788-a71d-42885d7bebb3` |
| `ListRunsOfScheduleResponse.data.ScheduleRun.scheduleRid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |
| `ListRunsOfScheduleResponse.data.ScheduleRun.scheduleVersionRid` | string | 是 | The RID of a schedule version |
| `ListRunsOfScheduleResponse.data.ScheduleRun.createdTime` | string | 是 | The time at which the schedule run was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ListRunsOfScheduleResponse.data.ScheduleRun.createdBy` | string | 否 | The Foundry user who manually invoked this schedule run. Automatic trigger runs have this field set to<br>empty.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListRunsOfScheduleResponse.data.ScheduleRun.result` | union | 否 | The result of triggering the schedule. If empty, it means the service<br>is still working on triggering the schedule. |
| `ListRunsOfScheduleResponse.data.ScheduleRun.result.ignored` | object | 否 | The schedule is not running as all targets are up-to-date. |
| `ListRunsOfScheduleResponse.data.ScheduleRun.result.submitted` | object | 否 | The schedule has been successfully triggered. |
| `ListRunsOfScheduleResponse.data.ScheduleRun.result.submitted.buildRid` | string | 是 | The RID of a Build.<br>示例: `ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58` |
| `ListRunsOfScheduleResponse.data.ScheduleRun.result.error` | object | 否 | An error occurred attempting to run the schedule. |
| `ListRunsOfScheduleResponse.data.ScheduleRun.result.error.errorName` | enum | 是 | 示例: `TARGETRESOLUTIONFAILURE` |
| `ListRunsOfScheduleResponse.data.ScheduleRun.result.error.description` | string | 是 | — |
| `ListRunsOfScheduleResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "scheduleVersionRid": "ri.scheduler.main.schedule-version.4d1eb55f-6c13-411c-a911-5d84e08d8017",
      "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
      "scheduleRid": "ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871",
      "createdTime": "2003-05-06T12:34:56.789Z",
      "rid": "ri.scheduler.main.run.d2a5e9c6-298d-4788-a71d-42885d7bebb3"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```
