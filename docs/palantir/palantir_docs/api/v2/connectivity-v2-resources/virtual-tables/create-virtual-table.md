`POST /api/v2/connectivity/connections/{connectionRid}/virtualTables`

Creates a new [Virtual Table](/docs/foundry/data-integration/virtual-tables/) from an upstream table. The VirtualTable will be created
in the specified parent folder and can be queried through Foundry's data access APIs.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:connectivity-virtual-table-write`.

**OAuth2 scopes**: `api:connectivity-virtual-table-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `connectionRid` | string | 是 | The Resource Identifier (RID) of a Connection (also known as a source).<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |

## Request body

```json
{
  "markings": [
    "18212f9a-0e63-4b79-96a0-aae04df23336"
  ],
  "parentRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791",
  "name": "my_table"
}
```

## Response

**VirtualTable**

The created VirtualTable

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `VirtualTable` | object | 是 | The created VirtualTable<br>示例: `{"markings":["18212f9a-0e63-4b79-96a0-aae04df23336"],"parentRid":"ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791","name":"my_table","rid":"ri.foundry.main.table.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da"}` |
| `VirtualTable.rid` | string | 是 | The Resource Identifier (RID) of a registered VirtualTable.<br>示例: `ri.foundry.main.table.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `VirtualTable.name` | string | 是 | The name of a VirtualTable.<br>示例: `my_table` |
| `VirtualTable.parentRid` | string | 是 | The unique resource identifier (RID) of a Folder.<br>示例: `ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791` |
| `VirtualTable.config` | union | 是 | — |
| `VirtualTable.config.snowflake` | object | 否 | Pointer to the table in Snowflake. Uses the Snowflake table identifier of database, schema and table. |
| `VirtualTable.config.snowflake.database` | string | 是 | The database name.<br>示例: `MY_DATABASE` |
| `VirtualTable.config.snowflake.schema` | string | 是 | The schema name.<br>示例: `PUBLIC` |
| `VirtualTable.config.snowflake.table` | string | 是 | The table name.<br>示例: `MY_TABLE` |
| `VirtualTable.config.unity` | object | 否 | Pointer to the table in Unity Catalog. Uses the Databricks table identifier of catalog, schema and table. |
| `VirtualTable.config.unity.catalog` | string | 是 | The catalog name.<br>示例: `my_catalog` |
| `VirtualTable.config.unity.schema` | string | 是 | The schema name.<br>示例: `default` |
| `VirtualTable.config.unity.table` | string | 是 | The table name.<br>示例: `my_table` |
| `VirtualTable.config.glue` | object | 否 | Pointer to the table in AWS Glue. |
| `VirtualTable.config.glue.database` | string | 是 | The database name.<br>示例: `my_database` |
| `VirtualTable.config.glue.table` | string | 是 | The table name.<br>示例: `my_table` |
| `VirtualTable.config.delta` | object | 否 | Pointer to the Delta table in cloud object storage (e.g., Azure Data Lake Storage, Google Cloud Storage, S3). |
| `VirtualTable.config.delta.path` | string | 是 | The path of the Delta table in object storage.<br>示例: `var/tmp/mytable` |
| `VirtualTable.config.iceberg` | object | 否 | Pointer to the Iceberg table. |
| `VirtualTable.config.iceberg.tableIdentifier` | string | 是 | The identifier of the Iceberg table.<br>示例: `mytable` |
| `VirtualTable.config.iceberg.warehousePath` | string | 否 | The path to the folder in the file system containing the Iceberg table. Can be omitted when the<br>connection is configured with a catalog that does not rely on warehouse path.<br>示例: `/var/tmp/iceberg` |
| `VirtualTable.config.files` | object | 否 | Pointer to the table in cloud object storage (e.g., Azure Data Lake Storage, Google Cloud Storage, S3). |
| `VirtualTable.config.files.format` | enum | 是 | The format of files in the upstream source.<br>示例: `PARQUET` |
| `VirtualTable.config.files.path` | string | 是 | Storage path for the data in the underlying file system, i.e. paths like `/foo/bar`. The scheme is not<br>included. May be either a folder or file. A non-partitioned table will have a single location. A<br>partitioned table can have multiple locations, one for each partition.<br>示例: `/var/tmp/myfolder` |
| `VirtualTable.config.bigquery` | object | 否 | Pointer to the table in BigQuery. Uses the BigQuery table identifier of project, dataset and table. |
| `VirtualTable.config.bigquery.project` | string | 是 | The BigQuery project name.<br>示例: `my-project` |
| `VirtualTable.config.bigquery.dataset` | string | 是 | The BigQuery dataset name.<br>示例: `mydataset` |
| `VirtualTable.config.bigquery.table` | string | 是 | The BigQuery table name.<br>示例: `mytable` |
| `VirtualTable.markings` | list<MarkingId> | 否 | — |
| `VirtualTable.markings.MarkingId` | string | 是 | The ID of a security marking. |

```json
{
  "markings": [
    "18212f9a-0e63-4b79-96a0-aae04df23336"
  ],
  "parentRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791",
  "name": "my_table",
  "rid": "ri.foundry.main.table.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidVirtualTableConnection` | The specified connection is invalid or inaccessible. |
| CONFLICT | `VirtualTableAlreadyExists` | A VirtualTable with the same name already exists in the parent folder. |
| PERMISSION_DENIED | `VirtualTableRegisterFromSourcePermissionDenied` | User lacks permission to use the specified connection for virtual table registration. |
| PERMISSION_DENIED | `CreateVirtualTablePermissionDenied` | Could not create the VirtualTable. |
| NOT_FOUND | `ConnectionNotFound` | The given Connection could not be found. |
