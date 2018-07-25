oracle.jdbc.pool.OracleDataSource ds = (oracle.jdbc.pool.OracleDataSource) muleContext.getRegistry().get("dbDataSource");
oracle.jdbc.OracleConnection conn = (oracle.jdbc.OracleConnection) ds.getConnection();
// backup payload
LinkedHashMap payloadBkp = new LinkedHashMap(payload);

// Create Struct Array
Object [] keyMemberList = payload.projectDetails.keyMemberList; 
oracle.sql.StructDescriptor keyMemberPropertyDesc = oracle.sql.StructDescriptor.createDescriptor("KEYMEMBERPROPERTY",conn);
Object[] keyMemberListStruct = new Object[payload.projectDetails.keyMemberList.size()];
int cntr = 0;
for (LinkedHashMap keyMember : keyMemberList) {
	Object[] keyMemberObj = new Object[3];
	keyMemberObj[0] = keyMember.get("personId");
	keyMemberObj[1] = keyMember.get("projectRoleType");
	keyMemberObj[2] = keyMember.get("startDate");
	oracle.sql.STRUCT keyMemberStruct = new oracle.sql.STRUCT(keyMemberPropertyDesc, conn, keyMemberObj);
	keyMemberListStruct[cntr++] = keyMemberStruct;
}

// Create Array of Structs
oracle.sql.ArrayDescriptor keyMemberPropertyTableDesc = oracle.sql.ArrayDescriptor.createDescriptor("KEYMEMBERPROPERTY_TABLE",conn);
oracle.sql.ARRAY keyMemberListArray = new oracle.sql.ARRAY(keyMemberPropertyTableDesc, conn, keyMemberListStruct);

//close connection
conn.close();

// return results
flowVars.keyMemberListArray = keyMemberListArray;
payload = payloadBkp;