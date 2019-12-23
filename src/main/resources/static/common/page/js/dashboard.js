$(document).ready(function () {
    getOverallCnt();
    $("#cluster_list").bootstrapTable("refresh")
    setInterval('getOverallCnt()', 60000);
});


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
        tableResponsive: true,
        exportDataType: "basic",            //导出表格方式（默认basic：只导出当前页的表格数据；all：导出所有数据；selected：导出选中的数据）
        exportTypes: ['csv', 'excel'], //导出文件类型
        dataType: "json",                   //服务器返回的数据类型
        contentType: "application/x-www-form-urlencoded", //发送到服务器的数据编码类型

        columns: [
            {
                title: 'URI',
                field: 'uri',
                align: 'left',
                width: 200,
                formatter: function (value, row, index) {
                    return '<a href="' + value + '" target="_Blank">' + value + '</a>'
                }
            },
            {
                title: 'System ID',
                field: 'sysId',
                align: 'left',
                width: 50
            },
            {
                title: 'Province',
                field: 'province',
                align: 'left',
                width: 70
            },
            {
                title: 'Flink Task Name',
                field: 'flinkTaskName',
                align: 'left',
                width: 100
            },
            {
                title: 'Running Job',
                field: 'runningJobCnt',
                align: 'left',
                width: 50
            },
            {
                title: 'Status',
                field: 'status',
                align: 'left',
                width: 100
            },
            {
                title: '操作',
                field: 'id',
                align: 'center',
                width: 100,
                formatter: function (value, row, index) {
                    let param = value.toString() + ',&quot;' + row.uri + '&quot;,&quot;' + row.name + '&quot;';
                    return '<span><a href="javascript:void(0)" id="del-btn-' + value + '" onclick="delCluster(' + value + ')">删除</a></span>'
                        + '<span> </span>'
                        + '<span><a href="javascript:void(0)" onclick="updateCluster(' + param + ')">修改</a></span>';
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
                align: 'left',
                width: 14
            },
            {
                title: 'value',
                field: 'value',
                align: 'left',
                width: 14
            }
        ]
    });

    $("#taskmanager_list").bootstrapTable({
        url: '/taskmanagers',                     //请求后台的URL
        method: 'get',                      //请求方式
        striped: true,                      //是否显示行间隔色
        pagination: true,                   //是否显示分页
        sidePagination: 'client',           //分页方式：client客户端分页，server服务端分页
        pageNumber: 1,                       //初始化显示第几页，默认第一页
        pageSize: 10,                       //每页显示数据的条数
        pageList: [5, 10, 20],              //可供选择的每页的行数

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
                align: 'left',
                width: 14,
                // formatter: function (value, row, index) {
                //     return '<a href="javascript:void(0)" onclick="getTmDetail(\'' + value + '\')">' + value + '</a>'
                // }
            },
            {
                title: 'path',
                field: 'path',
                align: 'left',
                width: 14
            },

            {
                title: 'LastHeartbeat',
                field: 'timeSinceLastHeartbeat',
                align: 'left',
                width: 14
            },
            {
                title: 'freeSlots',
                field: 'freeSlots',
                align: 'left',
                width: 14
            },
            {
                title: 'slotsNum',
                field: 'slotsNum',
                align: 'left',
                width: 14
            },
            {
                title: 'dataPort',
                field: 'dataPort',
                align: 'left',
                width: 14
            },
            {
                title: 'cpuCores',
                field: 'hardware.cpuCores',
                align: 'left',
                width: 14
            },
            {
                title: 'managedMem',
                field: 'hardware.managedMemory',
                align: 'left',
                width: 14
            },
            {
                title: 'freeMem',
                field: 'hardware.freeMemory',
                align: 'left',
                width: 14
            },
            {
                title: 'physicalMem',
                field: 'hardware.physicalMemory',
                align: 'left',
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
                align: 'left',
                width: 14
            },

            {
                title: 'Job Name',
                field: 'name',
                align: 'left',
                width: 14
            },
            {
                title: 'Start Time',
                field: 'start-time',
                align: 'left',
                width: 14
            },
            {
                title: 'Duration',
                field: 'duration',
                align: 'left',
                width: 14
            },
            {
                title: 'End Time',
                field: 'end-time',
                align: 'left',
                width: 14
            },
            {
                title: 'Tasks',
                field: 'tasks',
                align: 'left',
                width: 14,
                formatter: function (value, row, index) {
                    let html = '';
                    let status = ["TOTAL", "CREATED", "CANCELED", "RUNNING", "RECONCILING",
                        "DEPLOYING", "FAILED", "SCHEDULED", "CANCELING", "FINISHED"];
                    for (let i = 0; i < status.length; i++) {
                        html += '<span class="little-box ' + status[i].toLowerCase()
                            + '" data-toggle="popover" title="' + status[i]
                            + '"> '
                            + value[status[i]]
                            + ' </span>'
                    }
                    return html
                }
            },
            {
                title: 'Status',
                field: 'status',
                align: 'left',
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
                align: 'left',
                width: 5
            },

            {
                title: 'Job Name',
                field: 'name',
                align: 'left',
                width: 14
            },
            {
                title: 'Start Time',
                field: 'start-time',
                align: 'left',
                width: 14
            },
            {
                title: 'Duration',
                field: 'duration',
                align: 'left',
                width: 7
            },
            {
                title: 'End Time',
                field: 'end-time',
                align: 'left',
                width: 14
            },
            {
                title: 'Tasks',
                field: 'tasks',
                align: 'left',
                width: 30,
                formatter: function (value, row, index) {
                    let html = '';
                    let status = ["TOTAL", "CREATED", "CANCELED", "RUNNING", "RECONCILING",
                        "DEPLOYING", "FAILED", "SCHEDULED", "CANCELING", "FINISHED"];
                    for (let i = 0; i < status.length; i++) {
                        html += '<span class="little-box ' + status[i].toLowerCase()
                            + '" data-toggle="popover" title="' + status[i]
                            + '"> '
                            + value[status[i]]
                            + ' </span>'
                    }
                    return html
                }
            },
            {
                title: 'Status',
                field: 'status',
                align: 'left',
                width: 14
            }
        ]
    });

    $("#insert-btn").on('click', function () {
        $.post("/cluster", $("#insert-form").serialize(), function (response) {
            $("#myModal").modal("hide");
            getOverallCnt()
            $("#cluster_list").bootstrapTable("refresh")
        })
    });

    $("#edit-btn").on('click', function () {
        $.ajax({
            url: "/cluster",
            type: "PUT",
            data: $("#edit-form").serialize() + '&id=' + $(this).attr('current-id'),
            dataType: "json",
            success: function (result) {
                if (result) {
                    layer.msg("修改成功");
                    $("#myModal2").modal("hide");
                    refresh()
                } else {
                    layer.msg("修改失败");
                }
            }
        });
        getOverallCnt()
        $("#cluster_list").bootstrapTable("refresh")
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

    /**
     * 点击tab键刷新
     */
    $("#navi_overview").on('click', function () {
        getOverallCnt();
        $("#cluster_list").bootstrapTable("refresh");
    })
    $("#navi_clusters").on('click', function () {
        venderNavi('jm')
        $("#jmconfigtable").bootstrapTable("refresh");
    })
    $("#navi_taskmanagers").on('click', function () {
        venderNavi('tm')
        $("#taskmanager_list").bootstrapTable("refresh");
    })
    $("#navi_running_jobs").on('click', function () {
        venderNavi('running_job')
        $("#running_jobs_table").bootstrapTable("refresh");
    })
    $("#navi_completed_jobs").on('click', function () {
        venderNavi('completed_job')
        $("#completed_jobs_table").bootstrapTable("refresh");
    })
});


/**
 * 导航内容渲染
 * @param obj
 */
function venderNavi(obj) {
    $.ajax({
        url: "/cluster_name", method: "get", async: false, success: function (result) {
            let keys = Object.keys(result);

            let init = true
            let list = $('#' + obj + '_datasourcelist')
            list.empty()
            keys.forEach(function (key) {
                let li;
                if (init) {
                    li = $('<li/>')
                        .addClass(obj + '_ds')
                        .addClass('less-padding')
                        .addClass('active')
                        .attr('id', obj + '_ds_' + key)
                        .appendTo(list);
                    init = false;
                } else {
                    li = $('<li/>')
                        .addClass(obj + '_ds')
                        .addClass('less-padding')
                        .attr('id', obj + '_ds_' + key)
                        .appendTo(list);
                }

                let a = $('<a/>')
                    .attr('href', '#')
                    .text(result[key].name)
                    .addClass('less-padding')
                    .appendTo(li);

                /*标签*/
                if (obj === 'tm') {

                } else if (obj === 'running_job') {
                    // $('<span/>')
                    //     .attr('class', 'badge')
                    //     .text(result[key].runningJob)
                    //     .appendTo(a);
                } else if (obj === 'completed_job') {

                }

            });

        }
    });
}

/**
 * 删除集群信息
 * @param id
 */
function delCluster(id) {
    $("#del-btn-" + id).confirmation({
        placement: "bottom",        //弹层在哪里出现（默认top）
        title: "确定删除吗？",     //弹层展现的内容（默认Are you sure?）
        btnOkLabel: '确定',      //确认按钮的显示的内容（默认Yes）
        btnCancelLabel: '取消',  //取消按钮的显示的内容（默认No）
        onConfirm: function () {  //点击确认按钮的事件
            $.ajax({
                url: "/cluster/" + id,
                method: "DELETE",
                success: function (result) {
                    if (result) {
                        layer.msg("删除成功");
                        getOverallCnt()
                        $("#cluster_list").bootstrapTable("refresh")
                    } else {
                        layer.msg("删除失败");
                    }
                }
            });
        },
        onCancel: function () {    //点击取消按钮的事件
            $('#del-btn').confirmation('hide')   //隐藏弹层

        }
    })
    $("#del-btn-" + id).confirmation('show')

}


/**
 * 修改集群信息
 * @param id
 */
function updateCluster(value, uri, name) {
    $("#edit-uri").attr('value', uri);
    $("#edit-name").attr('value', name);
    $("#edit-btn").attr('current-id', value);
    $("#edit-form")[0].reset();
    $("#myModal2").modal("show");
}

/**
 * 查看详情
 */
function getTmDetail(id) {
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

