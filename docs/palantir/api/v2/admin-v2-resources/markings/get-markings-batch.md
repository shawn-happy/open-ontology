`POST /api/v2/admin/markings/getBatch`

Execute multiple get requests on Marking.

The maximum batch size for this endpoint is 500.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Request body

```json
[
  {
    "markingId": "18212f9a-0e63-4b79-96a0-aae04df23336"
  }
]
```

## Response

**GetMarkingsBatchResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetMarkingsBatchResponse` | object | 是 | 示例: `{"data":{"18212f9a-0e63-4b79-96a0-aae04df23336":{"createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","organization":"ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa","name":"PII","description":"Contains personally identifiable information about our customers","createdTime":"2003-05-06T12:34:56.789Z","id":"18212f9a-0e63-4b79-96a0-aae04df23336","categoryId":"0950264e-01c8-4e83-81a9-1a6b7f77621a"}}}` |
| `GetMarkingsBatchResponse.data` | map | 否 | — |
| `GetMarkingsBatchResponse.data.MarkingId` | string | 是 | The ID of a security marking. |
| `GetMarkingsBatchResponse.data.Marking` | object | 是 | — |
| `GetMarkingsBatchResponse.data.Marking.id` | string | 是 | The ID of a security marking.<br>示例: `18212f9a-0e63-4b79-96a0-aae04df23336` |
| `GetMarkingsBatchResponse.data.Marking.categoryId` | string | 是 | The ID of a marking category. For user-created categories, this will be a UUID. Markings associated with<br>Organizations are placed in a category with ID "Organization".<br>示例: `0950264e-01c8-4e83-81a9-1a6b7f77621a` |
| `GetMarkingsBatchResponse.data.Marking.name` | string | 是 | 示例: `PII` |
| `GetMarkingsBatchResponse.data.Marking.description` | string | 否 | 示例: `Contains personally identifiable information about our customers` |
| `GetMarkingsBatchResponse.data.Marking.organization` | string | 否 | If this marking is associated with an Organization, its RID will be populated here.<br>示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |
| `GetMarkingsBatchResponse.data.Marking.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `GetMarkingsBatchResponse.data.Marking.createdBy` | string | 否 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |

```json
{
  "data": {
    "18212f9a-0e63-4b79-96a0-aae04df23336": {
      "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
      "organization": "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa",
      "name": "PII",
      "description": "Contains personally identifiable information about our customers",
      "createdTime": "2003-05-06T12:34:56.789Z",
      "id": "18212f9a-0e63-4b79-96a0-aae04df23336",
      "categoryId": "0950264e-01c8-4e83-81a9-1a6b7f77621a"
    }
  }
}
```
