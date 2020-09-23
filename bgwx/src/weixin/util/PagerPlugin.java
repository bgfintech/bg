/**
 * 
 */
package weixin.util;

import java.util.List;

/**
 * TODO 分页插件
 * 
 * @author Liucx
 * @createTime 2016年4月5日 上午9:11:30
 * @package com.commom
 * @className PagerPlugin.java
 * @version
 */
public class PagerPlugin<T extends Object> {
	private int currentPageNumb = 1; // 当前多少页（当前页号）
	private int pageCount; // 共计多少页
	private int currentRowNumb=40;// 一页多少条
	private int rowCount; // 共计多少条
	private int pagerBeginRow; // 当前页开始行号
	private int pagerEndRow; // 当前页结束行号
	private List<T> list;// 数据
	private boolean hasNextPage;// 是否有下一页
	private boolean hasForwardPage;// 是否有上一页
	private int showJumpButtomCount = 5; // 显示跳页按钮个数（不大于pageCount）
	private int jumpButtomBegin;// 跳页按钮开始
	private int jumpButtomEnd;// 跳页按钮结束

	public PagerPlugin(int currentPageNumb, int rowCount) {
		init(currentPageNumb, rowCount);
	}
	public PagerPlugin(int currentPageNumb, int rowCount,int currentRowNumb) {
		init(currentPageNumb, rowCount,currentRowNumb);
	}

	public PagerPlugin() {
	}

	private PagerPlugin<T> init(int currentPageNumb, int rowCount) {
		this.rowCount = rowCount;
		this.currentPageNumb = currentPageNumb;
		if(this.currentRowNumb == 0) {
			this.currentRowNumb = 10;
		}
		/**
		 * 求共计多少页
		 */
		if(rowCount == 0) {
			this.pageCount = 1;
		}else {
			if (rowCount % currentRowNumb == 0) {
				this.pageCount = rowCount / currentRowNumb;
			} else {
				this.pageCount = rowCount / currentRowNumb + 1;
			}
		}
		/**
		 * 求当前页开始行号
		 */
		// this.pagerBeginRow = (this.currentPageNumb-1)*this.currentRowNumb;
		if (this.currentPageNumb == 1) {
			this.pagerBeginRow = 0;
		} else {
			this.pagerBeginRow = this.currentPageNumb * this.currentRowNumb
					- currentRowNumb + 1;
		}
		/**
		 * 求当前页结束行号
		 */
		if (this.currentPageNumb == 1) {
			this.pagerEndRow = currentRowNumb + pagerBeginRow;
		}else {
			this.pagerEndRow = currentRowNumb + pagerBeginRow -1;
		}
		/**
		 * 求是否有下一页、上一页
		 */
		if (this.pageCount == 1) {
			hasForwardPage = false;
			hasNextPage = false;
		} else {
			if (this.currentPageNumb == 1) {
				hasForwardPage = false;
				hasNextPage = true;
			} else if (this.currentPageNumb == this.pageCount) {
				hasForwardPage = true;
				hasNextPage = false;
			} else {
				hasForwardPage = true;
				hasNextPage = true;
			}
		}

		/**
		 * 求跳页按钮个数
		 */
		if (this.showJumpButtomCount > this.pageCount) {
			this.showJumpButtomCount = this.pageCount;
			this.jumpButtomEnd = this.pageCount;
		}

		if (jumpButtomEnd > pageCount) {
			jumpButtomEnd = pageCount;
			this.showJumpButtomCount = this.jumpButtomEnd
					- this.jumpButtomBegin;
		}

		/**
		 * 求跳页按钮开始与结束
		 */
		this.jumpButtomEnd = (int) Math
				.ceil((double) ((double) this.currentPageNumb / (double) showJumpButtomCount))
				* showJumpButtomCount;
		this.jumpButtomBegin = jumpButtomEnd - showJumpButtomCount;

		return this;

	}
	
	
	
	
	private PagerPlugin<T> init(int currentPageNumb, int rowCount,int currentRowNumb) {
		this.rowCount = rowCount;
		this.currentPageNumb = currentPageNumb;
		this.currentRowNumb = currentRowNumb;
		/**
		 * 求共计多少页
		 */
		if(rowCount == 0) {
			this.pageCount = 1;
		}else {
			if (rowCount % currentRowNumb == 0) {
				this.pageCount = rowCount / currentRowNumb;
			} else {
				this.pageCount = rowCount / currentRowNumb + 1;
			}
		}
		/**
		 * 求当前页开始行号
		 */
		// this.pagerBeginRow = (this.currentPageNumb-1)*this.currentRowNumb;
		if (this.currentPageNumb == 1) {
			this.pagerBeginRow = 0;
		} else {
			this.pagerBeginRow = this.currentPageNumb * this.currentRowNumb
					- currentRowNumb + 1;
		}
		/**
		 * 求当前页结束行号
		 */
		if (this.currentPageNumb == 1) {
			this.pagerEndRow = currentRowNumb + pagerBeginRow;
		}else {
			this.pagerEndRow = currentRowNumb + pagerBeginRow -1;
		}
		/**
		 * 求是否有下一页、上一页
		 */
		if (this.pageCount == 1) {
			hasForwardPage = false;
			hasNextPage = false;
		} else {
			if (this.currentPageNumb == 1) {
				hasForwardPage = false;
				hasNextPage = true;
			} else if (this.currentPageNumb == this.pageCount) {
				hasForwardPage = true;
				hasNextPage = false;
			} else {
				hasForwardPage = true;
				hasNextPage = true;
			}
		}
		
		/**
		 * 求跳页按钮个数
		 */
		if (this.showJumpButtomCount > this.pageCount) {
			this.showJumpButtomCount = this.pageCount;
			this.jumpButtomEnd = this.pageCount;
		}
		
		if (jumpButtomEnd > pageCount) {
			jumpButtomEnd = pageCount;
			this.showJumpButtomCount = this.jumpButtomEnd
					- this.jumpButtomBegin;
		}
		
		/**
		 * 求跳页按钮开始与结束
		 */
		this.jumpButtomEnd = (int) Math
				.ceil((double) ((double) this.currentPageNumb / (double) showJumpButtomCount))
				* showJumpButtomCount;
		this.jumpButtomBegin = jumpButtomEnd - showJumpButtomCount;
		
		return this;
		
	}

	public int getCurrentPageNumb() {
		return currentPageNumb;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getCurrentRowNumb() {
		return currentRowNumb;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getPagerBeginRow() {
		return pagerBeginRow;
	}

	public List<T> getList() {
		return list;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public boolean isHasForwardPage() {
		return hasForwardPage;
	}

	public int getShowJumpButtomCount() {
		return showJumpButtomCount;
	}

	public int getJumpButtomBegin() {
		return jumpButtomBegin;
	}

	public int getJumpButtomEnd() {
		return jumpButtomEnd;
	}

	public void setCurrentPageNumb(int currentPageNumb) {
		this.currentPageNumb = currentPageNumb;
	}

	private void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void setCurrentRowNumb(int currentRowNumb) {
		this.currentRowNumb = currentRowNumb;
	}

	private void setPagerBeginRow(int pagerBeginRow) {
		this.pagerBeginRow = pagerBeginRow;
	}

	private void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	private void setHasForwardPage(boolean hasForwardPage) {
		this.hasForwardPage = hasForwardPage;
	}

	private void setShowJumpButtomCount(int showJumpButtomCount) {
		this.showJumpButtomCount = showJumpButtomCount;
	}

	private void setJumpButtomBegin(int jumpButtomBegin) {
		this.jumpButtomBegin = jumpButtomBegin;
	}

	private void setJumpButtomEnd(int jumpButtomEnd) {
		this.jumpButtomEnd = jumpButtomEnd;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPagerEndRow() {
		return pagerEndRow;
	}

	private void setPagerEndRow(int pagerEndRow) {
		this.pagerEndRow = pagerEndRow;
	}
	
}
