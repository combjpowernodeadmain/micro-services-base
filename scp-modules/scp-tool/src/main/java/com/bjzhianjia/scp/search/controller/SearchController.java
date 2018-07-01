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

package com.bjzhianjia.scp.search.controller;

import com.bjzhianjia.scp.search.service.LuceneService;
import com.bjzhianjia.scp.security.api.vo.search.IndexObject;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author scp
 * @create 2018/3/5.
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private LuceneService luceneService;

    @RequestMapping(value = "", method = {RequestMethod.POST,RequestMethod.GET})
    public TableResultResponse<IndexObject> search(@RequestParam String word, @RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "15") Integer pageSize) {
        return luceneService.page(pageNumber, pageSize, word);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public ObjectRestResponse createIndexObject(@RequestBody IndexObject indexObject) {
        luceneService.save(indexObject);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/index", method = RequestMethod.DELETE)
    public ObjectRestResponse removeIndexObject(@RequestBody IndexObject indexObject) {
        luceneService.delete(indexObject);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/index", method = RequestMethod.PATCH)
    public ObjectRestResponse batchCreateIndexObject(@RequestBody List<IndexObject> indexObjects) {
        for (IndexObject object : indexObjects) {
            luceneService.save(object);
        }
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/index", method = RequestMethod.PUT)
    public ObjectRestResponse updateIndexObject(@RequestBody IndexObject indexObject) {
        luceneService.update(indexObject);
        return new ObjectRestResponse();
    }

}
