`GET /api/v2/orchestration/builds/{buildRid}/jobs`

Get the Jobs in the Build.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:orchestration-read`.

**OAuth2 scopes**: `api:orchestration-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `buildRid` | string | 是 | The RID of a Build.<br>示例: `ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListJobsOfBuildResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListJobsOfBuildResponse` | object | 是 | 示例: `{"data":[{"startedTime":"2003-05-06T12:34:56.789Z","jobStatus":"WAITING","buildRid":"ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58","finishedTime":"2003-05-06T12:34:56.789Z","rid":"ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448","latestAttemptStartTime":"2003-05-06T12:34:56.789Z"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListJobsOfBuildResponse.data` | list<Job> | 否 | — |
| `ListJobsOfBuildResponse.data.Job` | object | 是 | — |
| `ListJobsOfBuildResponse.data.Job.rid` | string | 是 | The RID of a Job.<br>示例: `ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448` |
| `ListJobsOfBuildResponse.data.Job.buildRid` | string | 是 | The RID of the Build that the Job belongs to.<br>示例: `ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58` |
| `ListJobsOfBuildResponse.data.Job.startedTime` | string | 是 | The time this job started waiting for the dependencies to be resolved.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ListJobsOfBuildResponse.data.Job.latestAttemptStartTime` | string | 否 | The time this job's latest attempt started running. This field may be empty or outdated if the job failed to start.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ListJobsOfBuildResponse.data.Job.finishedTime` | string | 否 | The time this job was finished.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ListJobsOfBuildResponse.data.Job.jobStatus` | enum | 是 | The status of the job.<br>示例: `WAITING` |
| `ListJobsOfBuildResponse.data.Job.outputs` | list<JobOutput> | 否 | Outputs of the Job. Only outputs with supported types are listed here; unsupported types are omitted.<br>Currently supported types are Dataset and Media Set outputs. |
| `ListJobsOfBuildResponse.data.Job.outputs.JobOutput` | union | 是 | Other types of Job Outputs exist in Foundry. Currently, only Dataset and Media Set are supported by the API. |
| `ListJobsOfBuildResponse.data.Job.outputs.JobOutput.datasetJobOutput` | object | 否 | — |
| `ListJobsOfBuildResponse.data.Job.outputs.JobOutput.datasetJobOutput.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `ListJobsOfBuildResponse.data.Job.outputs.JobOutput.datasetJobOutput.outputTransactionRid` | string | 否 | The Resource Identifier (RID) of a Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `ListJobsOfBuildResponse.data.Job.outputs.JobOutput.transactionalMediaSetJobOutput` | object | 否 | — |
| `ListJobsOfBuildResponse.data.Job.outputs.JobOutput.transactionalMediaSetJobOutput.mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |
| `ListJobsOfBuildResponse.data.Job.outputs.JobOutput.transactionalMediaSetJobOutput.transactionId` | string | 否 | 示例: `c24f22d9-aec4-4f55-9f5e-9316eb1c83aa` |
| `ListJobsOfBuildResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "startedTime": "2003-05-06T12:34:56.789Z",
      "jobStatus": "WAITING",
      "buildRid": "ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58",
      "finishedTime": "2003-05-06T12:34:56.789Z",
      "rid": "ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448",
      "latestAttemptStartTime": "2003-05-06T12:34:56.789Z"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```
