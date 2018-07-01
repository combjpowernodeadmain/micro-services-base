/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package com.bjzhianjia.scp.security.api.vo.search;


import java.io.Serializable;

/**
 * Description:索引对象
 *
 * @author scp
 **/
public class IndexObject implements Comparable<IndexObject>,Serializable{
	
	private Long id;

	private String title;

	private String keywords;

	private String descripton;

	private String postDate;

	private String url;

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getDescripton() {
		return descripton;
	}

	public void setDescripton(String descripton) {
		this.descripton = descripton;
	}



	/*相似度*/
	private float score;


	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getScore() {
		return score;
	}

	public  void setScore(float score) {
		this.score = score;
	}


	public IndexObject() {
		super();
	}

	public IndexObject(Long _id, String _keywords, String _descripton, String _postDate, float _score) {
		super();
		this.id = _id;
		this.keywords = _keywords;
		this.score = _score;
		this.descripton=_descripton;
		this.postDate=_postDate;
	}
	@Override
	public int compareTo(IndexObject o) {
		if(this.score < o.getScore()){
			return 1;
		}else if(this.score > o.getScore()){
			return -1;
		}
		return 0;
	}
	
	
}
