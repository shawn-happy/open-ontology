`GET /api/v2/orchestration/builds/{buildRid}`

Get the Build with the specified rid.

Users are allowed to make a maximum of **4 requests per second** and **25 concurrent requests**.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:orchestration-read`.

**OAuth2 scopes**: `api:orchestration-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `buildRid` | string | 是 | The RID of a Build.<br>示例: `ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58` |

## Response

**Build**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Build` | object | 是 | 示例: `{"abortOnFailure":false,"createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","retryBackoffDuration":{"unit":"SECONDS","value":30},"retryCount":1,"fallbackBranches":[],"scheduleRid":"ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871","branchName":"master","createdTime":"2003-05-06T12:34:56.789Z","jobRids":["ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448"],"finishedTime":"2003-05-06T12:34:56.789Z","rid":"ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58","status":"RUNNING"}` |
| `Build.rid` | string | 是 | The RID of a Build.<br>示例: `ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58` |
| `Build.branchName` | string | 是 | The branch that the build is running on.<br>示例: `master` |
| `Build.createdTime` | string | 是 | The timestamp that the build was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `Build.createdBy` | string | 是 | The user who created the build.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Build.fallbackBranches` | list<BranchName> | 否 | The branches to retrieve JobSpecs from if no JobSpec is found on the<br>target branch.<br>示例: `[]` |
| `Build.fallbackBranches.BranchName` | string | 是 | The name of a Branch. |
| `Build.jobRids` | list<JobRid> | 否 | — |
| `Build.jobRids.JobRid` | string | 是 | The RID of a Job. |
| `Build.retryCount` | integer | 是 | The number of retry attempts for failed Jobs within the Build. A Job's failure is not considered final until<br>all retries have been attempted or an error occurs indicating that retries cannot be performed. Be aware,<br>not all types of failures can be retried.<br>示例: `1` |
| `Build.retryBackoffDuration` | object | 是 | The duration to wait before retrying after a Job fails.<br>示例: `{"unit":"SECONDS","value":30}` |
| `Build.retryBackoffDuration.value` | integer | 是 | The duration value.<br>示例: `30` |
| `Build.retryBackoffDuration.unit` | enum | 是 | The unit of duration.<br>示例: `SECONDS` |
| `Build.abortOnFailure` | boolean | 是 | If any job in the build is unsuccessful, immediately finish the<br>build by cancelling all other jobs.<br>示例: `false` |
| `Build.status` | enum | 是 | The status of the build.<br>示例: `RUNNING` |
| `Build.finishedTime` | string | 否 | The time the build finished processing. Will be empty while the build is still running.<br>示例: `2003-05-06T12:34:56.789Z` |
| `Build.scheduleRid` | string | 否 | Schedule RID of the Schedule that triggered this build. If a user triggered the build, Schedule RID will be empty.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |

```json
{
  "abortOnFailure": false,
  "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "retryBackoffDuration": {
    "unit": "SECONDS",
    "value": 30
  },
  "retryCount": 1,
  "fallbackBranches": [],
  "scheduleRid": "ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871",
  "branchName": "master",
  "createdTime": "2003-05-06T12:34:56.789Z",
  "jobRids": [
    "ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448"
  ],
  "finishedTime": "2003-05-06T12:34:56.789Z",
  "rid": "ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58",
  "status": "RUNNING"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `BuildNotFound` | The given Build could not be found. |
