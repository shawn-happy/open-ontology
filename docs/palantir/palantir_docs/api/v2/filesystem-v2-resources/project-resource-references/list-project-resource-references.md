`GET /api/v2/filesystem/projects/{projectRid}/references`

List all references in the given project


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-read`.

**OAuth2 scopes**: `api:filesystem-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `projectRid` | string | 是 | The unique resource identifier (RID) of a Project.<br>示例: `ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `referenceType` | enum | 否 | Filter references by type. If not provided, all references are returned.<br>示例: `EXTERNAL` |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListProjectResourceReferencesResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListProjectResourceReferencesResponse` | object | 是 | 示例: `{"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListProjectResourceReferencesResponse.data` | list<ProjectResourceReference> | 否 | — |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference` | object | 是 | — |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference` | union | 是 | A [reference](/docs/foundry/security/projects-and-roles/#references) represents a resource from outside of<br>the current project that has been imported to the given project. |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference.external` | object | 否 | A reference to a resource that exists outside of the Foundry filesystem such as a spark profile or an LLM model. |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference.external.resourceRid` | string | 是 | The resource identifier of the external resource.<br>示例: `ri.spark-configuration-service.main.spark-profile.EXECUTOR_MEMORY_LARGE` |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference.external.name` | string | 是 | The user-provided label for this reference, used to identify the import within the project.<br>示例: `EXECUTOR_MEMORY_LARGE` |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference.external.importedAt` | string | 是 | 示例: `2025-01-01T00:00:00Z` |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference.external.importedBy` | string | 是 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference.filesystem` | object | 否 | A reference to a resource that exists within another project |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference.filesystem.resourceRid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference.filesystem.name` | string | 是 | The display name of the referenced resource.<br>示例: `my_resource` |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference.filesystem.importedAt` | string | 是 | 示例: `2025-01-01T00:00:00Z` |
| `ListProjectResourceReferencesResponse.data.ProjectResourceReference.reference.filesystem.importedBy` | string | 是 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |
| `ListProjectResourceReferencesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ProjectNotFound` | The given Project could not be found. |
