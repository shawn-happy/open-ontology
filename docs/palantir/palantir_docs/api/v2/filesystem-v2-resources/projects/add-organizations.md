`POST /api/v2/filesystem/projects/{projectRid}/addOrganizations`

Adds a list of Organizations to a Project.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-write`.

**OAuth2 scopes**: `api:filesystem-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `projectRid` | string | 是 | The unique resource identifier (RID) of a Project.<br>示例: `ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8` |

## Request body

```json
{
  "organizationRids": [
    "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `OrganizationsNotFound` | At least one organization RID could not be found. |
| INVALID_ARGUMENT | `InvalidOrganizationHierarchy` | Organizations on a project must also exist on the parent space. This error is thrown if the configuration<br>of a project's organizations (on creation or subsequently) results in the project being marked with either<br>no organizations in a marked space, or with an organization that is not present on the parent space. |
| PERMISSION_DENIED | `AddOrganizationsPermissionDenied` | Could not addOrganizations the Project. |
| NOT_FOUND | `ProjectNotFound` | The given Project could not be found. |
