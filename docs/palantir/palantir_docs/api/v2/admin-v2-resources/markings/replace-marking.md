`PUT /api/v2/admin/markings/{markingId}`

Replace the Marking with the specified id.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `markingId` | string | 是 | The ID of a security marking.<br>示例: `18212f9a-0e63-4b79-96a0-aae04df23336` |

## Request body

```json
{
  "name": "PII",
  "description": "Contains personally identifiable information about our customers"
}
```

## Response

**Marking**

The replaced Marking

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Marking` | object | 是 | The replaced Marking<br>示例: `{"createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","organization":"ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa","name":"PII","description":"Contains personally identifiable information about our customers","createdTime":"2003-05-06T12:34:56.789Z","id":"18212f9a-0e63-4b79-96a0-aae04df23336","categoryId":"0950264e-01c8-4e83-81a9-1a6b7f77621a"}` |
| `Marking.id` | string | 是 | The ID of a security marking.<br>示例: `18212f9a-0e63-4b79-96a0-aae04df23336` |
| `Marking.categoryId` | string | 是 | The ID of a marking category. For user-created categories, this will be a UUID. Markings associated with<br>Organizations are placed in a category with ID "Organization".<br>示例: `0950264e-01c8-4e83-81a9-1a6b7f77621a` |
| `Marking.name` | string | 是 | 示例: `PII` |
| `Marking.description` | string | 否 | 示例: `Contains personally identifiable information about our customers` |
| `Marking.organization` | string | 否 | If this marking is associated with an Organization, its RID will be populated here.<br>示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |
| `Marking.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `Marking.createdBy` | string | 否 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |

```json
{
  "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "organization": "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa",
  "name": "PII",
  "description": "Contains personally identifiable information about our customers",
  "createdTime": "2003-05-06T12:34:56.789Z",
  "id": "18212f9a-0e63-4b79-96a0-aae04df23336",
  "categoryId": "0950264e-01c8-4e83-81a9-1a6b7f77621a"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetMarkingCategoryPermissionDenied` | The provided token does not have permission to view the marking category. |
| INVALID_ARGUMENT | `MarkingNameInCategoryAlreadyExists` | A marking with the same name already exists in the category. |
| PERMISSION_DENIED | `GetMarkingPermissionDenied` | The provided token does not have permission to view the marking. |
| INVALID_ARGUMENT | `MarkingNameIsEmpty` | The marking name is empty. |
| PERMISSION_DENIED | `ReplaceMarkingPermissionDenied` | Could not replace the Marking. |
| NOT_FOUND | `MarkingNotFound` | The given Marking could not be found. |
