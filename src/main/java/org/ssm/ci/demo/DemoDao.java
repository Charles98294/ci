package org.ssm.ci.demo;

import java.util.List;

public interface DemoDao {

	List<?> queryList(Object param);

	int queryCount(Object param);

	Object queryRow(Object param);

	int insert(Object param);

	int update(Object param);

	int delete(Object param);
}