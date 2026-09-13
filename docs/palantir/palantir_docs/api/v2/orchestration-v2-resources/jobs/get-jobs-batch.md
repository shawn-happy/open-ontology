`POST /api/v2/orchestration/jobs/getBatch`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Execute multiple get requests on Job.

Users are allowed to make a maximum of **4 requests per second** and **25 concurrent requests**.


The maximum batch size for this endpoint is 500.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:orchestration-read`.

**OAuth2 scopes**: `api:orchestration-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
[
  {
    "jobRid": "ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448"
  }
]
```

## Response

**GetJobsBatchResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetJobsBatchResponse` | object | 是 | 示例: `{"data":{"ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448":{"startedTime":"2003-05-06T12:34:56.789Z","jobStatus":"WAITING","buildRid":"ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58","finishedTime":"2003-05-06T12:34:56.789Z","rid":"ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448","latestAttemptStartTime":"2003-05-06T12:34:56.789Z"}}}` |
| `GetJobsBatchResponse.data` | map | 否 | — |
| `GetJobsBatchResponse.data.JobRid` | string | 是 | The RID of a Job. |
| `GetJobsBatchResponse.data.Job` | object | 是 | — |
| `GetJobsBatchResponse.data.Job.rid` | string | 是 | The RID of a Job.<br>示例: `ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448` |
| `GetJobsBatchResponse.data.Job.buildRid` | string | 是 | The RID of the Build that the Job belongs to.<br>示例: `ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58` |
| `GetJobsBatchResponse.data.Job.startedTime` | string | 是 | The time this job started waiting for the dependencies to be resolved.<br>示例: `2003-05-06T12:34:56.789Z` |
| `GetJobsBatchResponse.data.Job.latestAttemptStartTime` | string | 否 | The time this job's latest attempt started running. This field may be empty or outdated if the job failed to start.<br>示例: `2003-05-06T12:34:56.789Z` |
| `GetJobsBatchResponse.data.Job.finishedTime` | string | 否 | The time this job was finished.<br>示例: `2003-05-06T12:34:56.789Z` |
| `GetJobsBatchResponse.data.Job.jobStatus` | enum | 是 | The status of the job.<br>示例: `WAITING` |
| `GetJobsBatchResponse.data.Job.outputs` | list<JobOutput> | 否 | Outputs of the Job. Only outputs with supported types are listed here; unsupported types are omitted.<br>Currently supported types are Dataset and Media Set outputs. |
| `GetJobsBatchResponse.data.Job.outputs.JobOutput` | union | 是 | Other types of Job Outputs exist in Foundry. Currently, only Dataset and Media Set are supported by the API. |
| `GetJobsBatchResponse.data.Job.outputs.JobOutput.datasetJobOutput` | object | 否 | — |
| `GetJobsBatchResponse.data.Job.outputs.JobOutput.datasetJobOutput.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `GetJobsBatchResponse.data.Job.outputs.JobOutput.datasetJobOutput.outputTransactionRid` | string | 否 | The Resource Identifier (RID) of a Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `GetJobsBatchResponse.data.Job.outputs.JobOutput.transactionalMediaSetJobOutput` | object | 否 | — |
| `GetJobsBatchResponse.data.Job.outputs.JobOutput.transactionalMediaSetJobOutput.mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |
| `GetJobsBatchResponse.data.Job.outputs.JobOutput.transactionalMediaSetJobOutput.transactionId` | string | 否 | 示例: `c24f22d9-aec4-4f55-9f5e-9316eb1c83aa` |

```json
{
  "data": {
    "ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448": {
      "startedTime": "2003-05-06T12:34:56.789Z",
      "jobStatus": "WAITING",
      "buildRid": "ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58",
      "finishedTime": "2003-05-06T12:34:56.789Z",
      "rid": "ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448",
      "latestAttemptStartTime": "2003-05-06T12:34:56.789Z"
    }
  }
}
```
