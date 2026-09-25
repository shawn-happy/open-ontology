`GET /api/v2/admin/markingCategories`

Maximum page size 100.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListMarkingCategoriesResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListMarkingCategoriesResponse` | object | 是 | 示例: `{"data":[{"categoryType":"CONJUNCTIVE","markings":["18212f9a-0e63-4b79-96a0-aae04df23336"],"createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","name":"Customer Data","description":"Markings related to data about our customers","createdTime":"2003-05-06T12:34:56.789Z","id":"0950264e-01c8-4e83-81a9-1a6b7f77621a","markingType":"MANDATORY"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListMarkingCategoriesResponse.data` | list<MarkingCategory> | 否 | — |
| `ListMarkingCategoriesResponse.data.MarkingCategory` | object | 是 | — |
| `ListMarkingCategoriesResponse.data.MarkingCategory.id` | string | 是 | The ID of a marking category. For user-created categories, this will be a UUID. Markings associated with<br>Organizations are placed in a category with ID "Organization".<br>示例: `0950264e-01c8-4e83-81a9-1a6b7f77621a` |
| `ListMarkingCategoriesResponse.data.MarkingCategory.name` | string | 是 | 示例: `Customer Data` |
| `ListMarkingCategoriesResponse.data.MarkingCategory.description` | string | 是 | 示例: `Markings related to data about our customers` |
| `ListMarkingCategoriesResponse.data.MarkingCategory.categoryType` | enum | 是 | 示例: `CONJUNCTIVE` |
| `ListMarkingCategoriesResponse.data.MarkingCategory.markingType` | enum | 是 | 示例: `MANDATORY` |
| `ListMarkingCategoriesResponse.data.MarkingCategory.markings` | list<MarkingId> | 否 | — |
| `ListMarkingCategoriesResponse.data.MarkingCategory.markings.MarkingId` | string | 是 | The ID of a security marking. |
| `ListMarkingCategoriesResponse.data.MarkingCategory.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ListMarkingCategoriesResponse.data.MarkingCategory.createdBy` | string | 否 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListMarkingCategoriesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "categoryType": "CONJUNCTIVE",
      "markings": [
        "18212f9a-0e63-4b79-96a0-aae04df23336"
      ],
      "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
      "name": "Customer Data",
      "description": "Markings related to data about our customers",
      "createdTime": "2003-05-06T12:34:56.789Z",
      "id": "0950264e-01c8-4e83-81a9-1a6b7f77621a",
      "markingType": "MANDATORY"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidPageSize` | The provided page size was zero or negative. Page sizes must be greater than zero. |
