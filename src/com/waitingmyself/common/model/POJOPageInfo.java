package com.waitingmyself.common.model;

import java.util.List;

/**
 * 普通对象的分页信息
 * 
 * @author 李旭光
 * @version 2010-7-26
 * @param <T>
 */
public class POJOPageInfo<T> implements IPageInfo<T> {

	/**
	 */
	private int count;

	/**
	 */
	private int rowsPerPage;

	/**
	 */
	private int nowPage;

	/**
	 */
	private List<T> items;

	public POJOPageInfo(int rowsPerPage, int nowPage) {
		this.rowsPerPage = rowsPerPage;
		this.nowPage = nowPage;
	}

	public POJOPageInfo(int rowsPerPage, int nowPage, int count) {
		this.rowsPerPage = rowsPerPage;
		this.nowPage = nowPage;
		this.setCount(count);
	}

	/**
	 * @return
	 */
	@Override
	public int getCount() {
		return this.count;
	}

	@Override
	public int getLimit() {
		return this.rowsPerPage;
	}

	@Override
	public int getStart() {
		return (this.nowPage - 1) * this.rowsPerPage;
	}

	/**
	 * @param  count
	 */
	@Override
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void setItems(List<T> items) {
		this.items = items;
	}

	@Override
	public List<T> getItems() {
		return items;
	}

	/**
	 * @return   the rowsPerPage
	 */
	public int getRowsPerPage() {
		return rowsPerPage;
	}

	/**
	 * @return   the nowPage
	 */
	public int getNowPage() {
		return nowPage;
	}

}
