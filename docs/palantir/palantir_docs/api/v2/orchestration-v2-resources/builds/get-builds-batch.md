`POST /api/v2/orchestration/builds/getBatch`

Execute multiple get requests on Build.

Users are allowed to make a maximum of **4 requests per second** and **25 concurrent requests**.


The maximum batch size for this endpoint is 100.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:orchestration-read`.

**OAuth2 scopes**: `api:orchestration-read`

## Request body

```json
[
  {
    "buildRid": "ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58"
  }
]
```

## Response

**GetBuildsBatchResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetBuildsBatchResponse` | object | 是 | 示例: `{"data":{"ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58":{"abortOnFailure":false,"createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","retryBackoffDuration":{"unit":"SECONDS","value":30},"retryCount":1,"fallbackBranches":[],"scheduleRid":"ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871","branchName":"master","createdTime":"2003-05-06T12:34:56.789Z","jobRids":["ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448"],"finishedTime":"2003-05-06T12:34:56.789Z","rid":"ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58","status":"RUNNING"}}}` |
| `GetBuildsBatchResponse.data` | map | 否 | — |
| `GetBuildsBatchResponse.data.BuildRid` | string | 是 | The RID of a Build. |
| `GetBuildsBatchResponse.data.Build` | object | 是 | — |
| `GetBuildsBatchResponse.data.Build.rid` | string | 是 | The RID of a Build.<br>示例: `ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58` |
| `GetBuildsBatchResponse.data.Build.branchName` | string | 是 | The branch that the build is running on.<br>示例: `master` |
| `GetBuildsBatchResponse.data.Build.createdTime` | string | 是 | The timestamp that the build was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `GetBuildsBatchResponse.data.Build.createdBy` | string | 是 | The user who created the build.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `GetBuildsBatchResponse.data.Build.fallbackBranches` | list<BranchName> | 否 | The branches to retrieve JobSpecs from if no JobSpec is found on the<br>target branch.<br>示例: `[]` |
| `GetBuildsBatchResponse.data.Build.fallbackBranches.BranchName` | string | 是 | The name of a Branch. |
| `GetBuildsBatchResponse.data.Build.jobRids` | list<JobRid> | 否 | — |
| `GetBuildsBatchResponse.data.Build.jobRids.JobRid` | string | 是 | The RID of a Job. |
| `GetBuildsBatchResponse.data.Build.retryCount` | integer | 是 | The number of retry attempts for failed Jobs within the Build. A Job's failure is not considered final until<br>all retries have been attempted or an error occurs indicating that retries cannot be performed. Be aware,<br>not all types of failures can be retried.<br>示例: `1` |
| `GetBuildsBatchResponse.data.Build.retryBackoffDuration` | object | 是 | The duration to wait before retrying after a Job fails.<br>示例: `{"unit":"SECONDS","value":30}` |
| `GetBuildsBatchResponse.data.Build.retryBackoffDuration.value` | integer | 是 | The duration value.<br>示例: `30` |
| `GetBuildsBatchResponse.data.Build.retryBackoffDuration.unit` | enum | 是 | The unit of duration.<br>示例: `SECONDS` |
| `GetBuildsBatchResponse.data.Build.abortOnFailure` | boolean | 是 | If any job in the build is unsuccessful, immediately finish the<br>build by cancelling all other jobs.<br>示例: `false` |
| `GetBuildsBatchResponse.data.Build.status` | enum | 是 | The status of the build.<br>示例: `RUNNING` |
| `GetBuildsBatchResponse.data.Build.finishedTime` | string | 否 | The time the build finished processing. Will be empty while the build is still running.<br>示例: `2003-05-06T12:34:56.789Z` |
| `GetBuildsBatchResponse.data.Build.scheduleRid` | string | 否 | Schedule RID of the Schedule that triggered this build. If a user triggered the build, Schedule RID will be empty.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |

```json
{
  "data": {
    "ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58": {
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
  }
}
```
