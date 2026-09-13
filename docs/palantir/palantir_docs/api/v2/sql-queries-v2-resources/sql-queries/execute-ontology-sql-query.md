`POST /api/v2/sqlQueries/executeOntology`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Executes a SQL query against the Ontology. Results are returned synchronously in
[Apache Arrow](https://arrow.apache.org/) format.


Third-party applications using this endpoint via OAuth2 must request the following operation scopes: `api:sql-queries-execute api:ontologies-read`.

**OAuth2 scopes**: `api:sql-queries-execute` `api:ontologies-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "query": "SELECT * FROM ri.ontology.main.object-type.xxx",
  "scenarioRid": "ri.actions..scenario.0000-0000",
  "branch": "ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252",
  "ontologyIdentifier": "example-ontology"
}
```

## Response

**body**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | — |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `QueryParseError` | The query cannot be parsed. |
| INTERNAL | `OntologyQueryFailed` | The Ontology query failed. |
| INVALID_ARGUMENT | `OntologyQueryInvalidObjectBackend` | The ontology query references object types or link types indexed in Object<br>Storage V1, which is incompatible with Ontology SQL. Migrate the entities<br>to Object Storage V2 or remove them from the query. |
| INVALID_ARGUMENT | `OntologyQueryNestedObjectSetTooLarge` | The query references too many objects across joins, link lookups, or<br>sub-queries. Narrow the scope (add filters, reduce joins, restrict<br>object types) and retry. The actual and maximum object counts are<br>returned as parameters. |
| INVALID_ARGUMENT | `OntologyQueryStringColumnTooLong` | A string column in the query result contains a value larger than<br>the platform's per-cell size limit. Exclude or filter the column,<br>or scope the query to skip the oversized rows. |
| NOT_FOUND | `OntologyObjectTypeNotFound` | The ontology query referenced an object type RID that does not exist or<br>is not visible to the requesting user. Verify the RID (e.g. via<br>list-object-types or get-object-type-details) and retry. |
| PERMISSION_DENIED | `ExecuteOntologySqlQueryPermissionDenied` | Could not executeOntology the SqlQuery. |
