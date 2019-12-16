venderNavi();

$(function () {
    $("#cluster_list").bootstrapTable({
        url: '/cluster',                     //请求后台的URL
        method: 'get',                      //请求方式
        toolbar: '#toolbar',                //工具按钮栏
        striped: true,                      //是否显示行间隔色
        pagination: true,                   //是否显示分页
        sidePagination: 'server',           //分页方式：client客户端分页，server服务端分页
        pageNumber: 1,                       //初始化显示第几页，默认第一页
        pageSize: 10,                       //每页显示数据的条数
        pageList: [5, 10, 20],              //可供选择的每页的行数
        queryParams: function (params) {
            return {
                page: params.pageNumber,
                size: params.pageSize
            };
        },                                  //请求后台传递的参数
        queryParamsType: '',
        uniqueId: 'id',
        showColumns: true,                  //是否展示所有列
        // showHeader: true,                    //是否展示表头
        showRefresh: true,                  //是否显示刷新按钮
        clickToSelect: true,                //是否启用点击选中行
        detailView: false,                  //是否显示父子表
        singleSelect: true,                 //是否只支持单选
        showExport: true,                    //是否展示导出按钮
        exportDataType: "basic",            //导出表格方式（默认basic：只导出当前页的表格数据；all：导出所有数据；selected：导出选中的数据）
        exportTypes: ['csv', 'excel'], //导出文件类型
        dataType: "json",                   //服务器返回的数据类型
        contentType: "application/x-www-form-urlencoded", //发送到服务器的数据编码类型
        // rowStyle: function (row, index) {
        //     //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
        //     var style = {};
        //     style={classes:'danger',css:{'color':'#ed5565'}};
        //     return style;
        // },

        columns: [
            // {
            //     title: 'id',
            //     field: 'id',
            //     align: 'center',
            //     valign: 'middle',
            //     width: 14
            // },
            {
                title: 'address',
                field: 'address',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'port',
                field: 'port',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'name',
                field: 'name',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: '操作',
                field: 'id',
                align: 'center',
                valign: "middle",
                width: 14,
                formatter: function (value, row, index) {
                    return '<a href="javascript:void(0)" onclick="delCluster(' + value + ')"><i class="icon-download-alt"></i>删除</a>'
                }
            }
        ]
    });

    $('#jmconfigtable').bootstrapTable({
        url: '/jobmanager/config',                     //请求后台的URL
        method: 'get',                      //请求方式

        queryParams: function () {
            if ($('li.active.jm_ds').attr('id') !== undefined) {
                return {
                    id: $('li.active.jm_ds').attr('id').substr(6)
                };
            }
        },                                  //请求后台传递的参数
        showColumns: true,                  //是否展示所有列
        dataType: "json",                   //服务器返回的数据类型
        contentType: "application/x-www-form-urlencoded", //发送到服务器的数据编码类型
        columns: [
            {
                title: 'key',
                field: 'key',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'value',
                field: 'value',
                align: 'center',
                valign: 'middle',
                width: 14
            }
        ]
    });

    $("#taskmanager_list").bootstrapTable({
        url: '/taskmanagers',                     //请求后台的URL
        method: 'get',                      //请求方式
        striped: true,                      //是否显示行间隔色
        queryParams: function () {
            if ($('li.active.tm_ds').attr('id') !== undefined) {
                return {
                    id: $('li.active.tm_ds').attr('id').substr(6)
                };
            }
        },                                 //请求后台传递的参数
        queryParamsType: '',
        uniqueId: 'id',
        showColumns: true,                  //是否展示所有列
        dataType: "json",                   //服务器返回的数据类型
        contentType: "application/x-www-form-urlencoded", //发送到服务器的数据编码类型
        columns: [
            {
                title: 'id',
                field: 'id',
                align: 'center',
                valign: 'middle',
                width: 14,
                // formatter: function (value, row, index) {
                //     return '<a href="javascript:void(0)" onclick="getTmDetail(\'' + value + '\')">' + value + '</a>'
                // }
            },
            {
                title: 'path',
                field: 'path',
                align: 'center',
                valign: 'middle',
                width: 14
            },

            {
                title: 'LastHeartbeat',
                field: 'timeSinceLastHeartbeat',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'freeSlots',
                field: 'freeSlots',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'slotsNum',
                field: 'slotsNum',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'dataPort',
                field: 'dataPort',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'cpuCores',
                field: 'hardware.cpuCores',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'managedMem',
                field: 'hardware.managedMemory',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'freeMem',
                field: 'hardware.freeMemory',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'physicalMem',
                field: 'hardware.physicalMemory',
                align: 'center',
                valign: 'middle',
                width: 14
            }
        ]
    });

    $("#running_jobs_table").bootstrapTable({
        url: '/jobs/running_list',                     //请求后台的URL
        method: 'get',                      //请求方式
        striped: true,                      //是否显示行间隔色
        queryParams: function () {
            if ($('li.active.running_job_ds').attr('id') !== undefined) {
                return {
                    id: $('li.active.running_job_ds').attr('id').substr(15)
                };
            }
        },                                 //请求后台传递的参数
        queryParamsType: '',
        showColumns: true,                  //是否展示所有列
        dataType: "json",                   //服务器返回的数据类型
        contentType: "application/x-www-form-urlencoded", //发送到服务器的数据编码类型
        columns: [
            {
                title: '所属集群',
                field: 'cluster',
                align: 'center',
                valign: 'middle',
                width: 14
            },

            {
                title: 'Job Name',
                field: 'name',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'Start Time',
                field: 'start-time',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'Duration',
                field: 'duration',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'End Time',
                field: 'end-time',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'Tasks',
                field: 'tasks',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'Status',
                field: 'status',
                align: 'center',
                valign: 'middle',
                width: 14
            }
        ]
    });

    $("#completed_jobs_table").bootstrapTable({
        url: '/jobs/completed_list',                     //请求后台的URL
        method: 'get',                      //请求方式
        striped: true,                      //是否显示行间隔色
        queryParams: function () {
            if ($('li.active.completed_job_ds').attr('id') !== undefined) {
                return {
                    id: $('li.active.completed_job_ds').attr('id').substr(17)
                };
            }
        },                                 //请求后台传递的参数
        queryParamsType: '',
        showColumns: true,                  //是否展示所有列
        dataType: "json",                   //服务器返回的数据类型
        contentType: "application/x-www-form-urlencoded", //发送到服务器的数据编码类型
        columns: [
            {
                title: '所属集群',
                field: 'cluster',
                align: 'center',
                valign: 'middle',
                width: 14
            },

            {
                title: 'Job Name',
                field: 'name',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'Start Time',
                field: 'start-time',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'Duration',
                field: 'duration',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'End Time',
                field: 'end-time',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'Tasks',
                field: 'tasks',
                align: 'center',
                valign: 'middle',
                width: 14
            },
            {
                title: 'Status',
                field: 'status',
                align: 'center',
                valign: 'middle',
                width: 14
            }
        ]
    });

    $("#insert-btn").on('click', function () {
        $.post("/cluster", $("#insert-form").serialize(), function (response) {
            $("#myModal").modal("hide");
            venderNavi()
            $("#cluster_list").bootstrapTable("refresh");
            $("#jmconfigtable").bootstrapTable("refresh");
            $("#taskmanager_list").bootstrapTable("refresh");
            $("#running_jobs_table").bootstrapTable("refresh");
            $("#completed_jobs_table").bootstrapTable("refresh");
        })
    });

    /**
     * 切换数据源及active效果
     */
    $('#jm_datasourcelist').on('click', 'li', function () {
        $(this).parent().find('li.active').removeClass('active');
        $(this).addClass('active');
        $('#logheading').html($(this).children().text() + 'jobmanager log')
        $("#jmconfigtable").bootstrapTable("refresh");
    })

    $('#tm_datasourcelist').on('click', 'li', function () {
        $(this).parent().find('li.active').removeClass('active');
        $(this).addClass('active');
        $("#taskmanager_list").bootstrapTable("refresh");
    })

    $('#running_job_datasourcelist').on('click', 'li', function () {
        $(this).parent().find('li.active').removeClass('active');
        $(this).addClass('active');
        $("#running_jobs_table").bootstrapTable("refresh");
    })
    $('#completed_job_datasourcelist').on('click', 'li', function () {
        $(this).parent().find('li.active').removeClass('active');
        $(this).addClass('active');
        $("#completed_jobs_table").bootstrapTable("refresh");
    })
});


/**
 * 导航内容渲染
 * @param obj
 */
function venderNavi() {
    $.ajax({
        url: "/cluster_name", method: "get", async: false, success: function (result) {
            let keys = Object.keys(result);
            //jmconfig navi
            let init = true
            let list = $('#jm_datasourcelist')
            list.empty()
            keys.forEach(function (key) {
                let li;
                if (init) {
                    init = false
                    li = $('<li/>')
                        .addClass('active')
                        .addClass('jm_ds')
                        .attr('id', 'jm_ds_' + key)
                        .appendTo(list);
                } else {
                    li = $('<li/>')
                        .addClass('jm_ds')
                        .attr('id', 'jm_ds_' + key)
                        .appendTo(list);
                }


                $('<a/>')
                    .attr('href', '#')
                    .text(result[key])
                    .appendTo(li);
            });

            //other navi
            let objs = ['tm', 'running_job', 'completed_job']
            objs.forEach(function (obj) {
                let list = $('#' + obj + '_datasourcelist')
                list.empty()
                let all = $('<li/>')
                    .addClass(obj + '_ds')
                    .addClass('active')
                    .attr('id', obj + '_ds_-1')
                    .appendTo(list);
                $('<a/>')
                    .attr('href', '#')
                    .text('All')
                    .appendTo(all);
                keys.forEach(function (key) {
                    let li;
                    li = $('<li/>')
                        .addClass(obj + '_ds')
                        .attr('id', obj + '_ds_' + key)
                        .appendTo(list);

                    $('<a/>')
                        .attr('href', '#')
                        .text(result[key])
                        .appendTo(li);
                });
            })
        }
    });
}

/**
 * 删除集群信息
 * @param id
 */
function delCluster(id) {
    $.ajax({
        url: "/cluster/" + id,
        method: "DELETE",
        success: function (result) {
            if (result) {
                layer.msg("删除成功");
                venderNavi();
                $("#cluster_list").bootstrapTable("refresh");
                $("#jmconfigtable").bootstrapTable("refresh");
                $("#taskmanager_list").bootstrapTable("refresh");
                $("#running_jobs_table").bootstrapTable("refresh");
                $("#completed_jobs_table").bootstrapTable("refresh");

            } else {
                layer.msg("删除失败");
            }
        }
    });
}

/**
 * 查看详情
 */
function getTmDetail(id) {
    alert(1);
    window.location.href = "/detail?id=" + id;
}

function getOverallCnt() {
    let cnt = null;
    $.ajax({
        url: "/overall_count", method: "get", async: false, success: function (result) {
            cnt = result;
        }
    })
    if (!cnt) {
        $("#total_cluster_cnt").text("N/A");
        $("#running_cluster_cnt").text("N/A");
        $("#running_tm_cnt").text("N/A");
        $("#running_job_cnt").text("N/A");
        $("#finished_job_cnt").text("N/A");
        $("#canceled_job_cnt").text("N/A");
        $("#failed_job_cnt").text("N/A");
    } else {
        let json = cnt;
        $("#total_cluster_cnt").text(json.totcalCluster);
        $("#running_cluster_cnt").text(json.runningCluster);
        $("#running_tm_cnt").text(json.runningTaskManager);
        $("#running_job_cnt").text(json.runningJob);
        $("#finished_job_cnt").text(json.completedJob);
        $("#canceled_job_cnt").text(json.canceledJob);
        $("#failed_job_cnt").text(json.failedJob)
    }
}

$(document).ready(function () {
    getOverallCnt();
    $("#taskmanager_list").bootstrapTable("refresh")
    $("#running_jobs_table").bootstrapTable("refresh")
    $("#completed_jobs_table").bootstrapTable("refresh")
    setInterval('getOverallCnt()', 1000);
    setInterval('$("#running_jobs_table").bootstrapTable("refresh")', 1000);
});

