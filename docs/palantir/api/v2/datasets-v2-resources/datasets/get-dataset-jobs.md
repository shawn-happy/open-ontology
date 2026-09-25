`POST /api/v2/datasets/{datasetRid}/jobs`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get the RIDs of the Jobs for the given dataset. By default, returned Jobs are sorted in descending order by the Job start time.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-read`.

**OAuth2 scopes**: `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | The name of the Branch. If none is provided, the default Branch name - `master` for most enrollments - will be used.<br>示例: `master` |
| `pageSize` | integer | 否 | Max number of results to return. A limit of 1000 on if no limit is supplied in the search request |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "orderBy": [
    {
      "sortType": "BY_STARTED_TIME",
      "sortDirection": "DESCENDING"
    }
  ],
  "where": {
    "type": "timeFilter",
    "field": "SUBMITTED_TIME",
    "comparisonType": "GTE",
    "value": "2020-09-30T14:30:00Z"
  }
}
```

## Response

**GetJobResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetJobResponse` | object | 是 | 示例: `{"data":[{"jobRid":"ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `GetJobResponse.data` | list<JobDetails> | 否 | — |
| `GetJobResponse.data.JobDetails` | object | 是 | — |
| `GetJobResponse.data.JobDetails.jobRid` | string | 是 | The RID of a Job.<br>示例: `ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448` |
| `GetJobResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "jobRid": "ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `BranchNotFound` | The requested branch could not be found, or the client token does not have access to it. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| PERMISSION_DENIED | `GetDatasetJobsPermissionDenied` | Could not jobs the Dataset. |
