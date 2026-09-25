`GET /api/v2/audit/organizations/{organizationRid}/logFiles`

Lists all LogFiles.

This is a paged endpoint. Each page may be smaller or larger than the requested page size. However, it is guaranteed that if there are more results available, the `nextPageToken` field will be populated. To get the next page, make the same request again, but set the value of the `pageToken` query parameter to be value of the `nextPageToken` value of the previous response. If there is no `nextPageToken` field in the response, you are on the last page.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:audit-read`.

**OAuth2 scopes**: `api:audit-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `organizationRid` | string | 是 | 示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `startDate` | string | 否 | List log files for audit events starting from this date. This parameter is required for the initial request (when `pageToken` is not provided).<br>示例: `2024-01-01` |
| `endDate` | string | 否 | List log files for audit events up until this date (inclusive). If absent, defaults to no end date. Use the returned `nextPageToken` to continually poll the  `listLogFiles` endpoint to list the latest available logs.<br>示例: `2025-01-01` |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListLogFilesResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListLogFilesResponse` | object | 是 | 示例: `{"data":[{"id":"S2VlcEV4cGxvcmluZw=="}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListLogFilesResponse.data` | list<LogFile> | 否 | — |
| `ListLogFilesResponse.data.LogFile` | object | 是 | — |
| `ListLogFilesResponse.data.LogFile.id` | string | 是 | The ID of an audit log file<br>示例: `S2VlcEV4cGxvcmluZw==` |
| `ListLogFilesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "id": "S2VlcEV4cGxvcmluZw=="
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `ListLogFilesPermissionDenied` | The provided token does not have permission to list audit log files. |
| INVALID_ARGUMENT | `MissingStartDate` | Start date is required to list audit log files. |
