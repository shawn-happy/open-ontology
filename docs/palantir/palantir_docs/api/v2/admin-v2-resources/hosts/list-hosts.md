`GET /api/v2/admin/enrollments/{enrollmentRid}/hosts`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Lists all Hosts.

This is a paged endpoint. Each page may be smaller or larger than the requested page size. However, it is guaranteed that if there are more results available, the `nextPageToken` field will be populated. To get the next page, make the same request again, but set the value of the `pageToken` query parameter to be value of the `nextPageToken` value of the previous response. If there is no `nextPageToken` field in the response, you are on the last page.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `enrollmentRid` | string | 是 | 示例: `ri.control-panel.main.customer.466f812b-f974-4478-9d4f-90402cd3def6` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ListHostsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListHostsResponse` | object | 是 | 示例: `{"data":[{"hostName":"example.palantirfoundry.com"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListHostsResponse.data` | list<Host> | 否 | — |
| `ListHostsResponse.data.Host` | object | 是 | — |
| `ListHostsResponse.data.Host.hostName` | string | 是 | 示例: `example.palantirfoundry.com` |
| `ListHostsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "hostName": "example.palantirfoundry.com"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `ListHostsPermissionDenied` | You do not have permission to list hosts for this enrollment |
| INVALID_ARGUMENT | `InvalidPageSize` | The provided page size was zero or negative. Page sizes must be greater than zero. |
| NOT_FOUND | `EnrollmentNotFound` | The given Enrollment could not be found. |
