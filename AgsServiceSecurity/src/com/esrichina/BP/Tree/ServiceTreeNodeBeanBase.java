package com.esrichina.BP.Tree;

public abstract class ServiceTreeNodeBeanBase extends CatlogTreeNodeBeanBase{

	DatasourceBase dataSource;

	public DatasourceBase getDataSource() {
		return dataSource;
	}

	public void setDataSource(DatasourceBase dataSource) {
		this.dataSource = dataSource;
	}
	
	
}
