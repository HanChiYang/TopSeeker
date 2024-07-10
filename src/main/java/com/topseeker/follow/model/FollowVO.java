package com.topseeker.follow.model;


import com.topseeker.follow.model.FollowVO.CompositeDetail;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
 
@Entity
@Table(name = "follow")
@IdClass(CompositeDetail.class)
public class FollowVO {
	
	
	@Id 
	@Column(name = "mem_no")
	private Integer memNoA;
	
	@Id 
	@Column(name = "be_followed_mem_no")
	private Integer memNoB;

    
	public CompositeDetail getCompositeKey() {
		return new CompositeDetail(memNoA, memNoB);
	}
	
	public void setCompositeKey(CompositeDetail key) {
		this.memNoA = key.getMemNoA();
		this.memNoB = key.getMemNoB();
	}

	public Integer getMemNoA() {
		return memNoA;
	}

	public void setMemNoA(Integer memNoA) {
		this.memNoA = memNoA;
	}

	public Integer getMemNoB() {
		return memNoB;
	}

	public void setMemNoB(Integer memNoB) {
		this.memNoB = memNoB;
	}
	
	static class CompositeDetail implements Serializable {
		private static final long serialVersionUID = 1L;

		private Integer memNoA;
		private Integer memNoB;

		
		public CompositeDetail() {
			super();
		}

		public CompositeDetail(Integer memNoA, Integer memNoB) {
			super();
			this.memNoA = memNoA;
			this.memNoB = memNoB;
		}

		public Integer getMemNoA() {
			return memNoA;
		}

		public void setMemNoA(Integer memNoA) {
			this.memNoA = memNoA;
		}

		public Integer getMemNoB() {
			return memNoB;
		}

		public void setMemNoB(Integer memNoB) {
			this.memNoB = memNoB;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((memNoA == null) ? 0 : memNoA.hashCode());
			result = prime * result + ((memNoB == null) ? 0 : memNoB.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;

			if (obj != null && getClass() == obj.getClass()) {
				CompositeDetail compositeKey = (CompositeDetail) obj;
				if (memNoA.equals(compositeKey.memNoA) && memNoB.equals(compositeKey.memNoB)) {
					return true;
				}
			}

			return false;
		}

	
	}
	
}	
	

