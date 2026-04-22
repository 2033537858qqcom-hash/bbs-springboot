package com.liang.bbs.user.service.mapstruct;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 */
public interface CommonMS<P, D> {
    /**
     * po杞琩to
     *
     * @param p po
     * @return dto
     */
    D toDTO(P p);

    /**
     * dto杞琾o
     *
     * @param d dto
     * @return po
     */
    P toPo(D d);

    /**
     * po杞琩to
     *
     * @param pList po
     * @return dto
     */
    List<D> toDTO(List<P> pList);

    /**
     * dto杞琾o
     *
     * @param pList dto
     * @return po
     */
    List<P> toPo(List<D> pList);

    /**
     * po杞琩to
     *
     * @param pageInfo po
     * @return dto
     */
    PageInfo<D> toPage(PageInfo<P> pageInfo);

}
