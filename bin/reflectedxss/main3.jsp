<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <style>
            .form {
                border: #323232 solid 1px;
                margin: auto;
                padding: 10px;
                width: 70vw;
                height: 100%;
            }

            .form__input {
                display: flex;
                flex-direction: column;
                gap: 1rem;
            }

            .form__input>div {
                display: flex;
                justify-content: space-between;
            }

            .form__button {
                margin-top: 10px;
                width: 100%;
            }

            .container {
                position: relative;
                overflow-x: auto;
                border: #323232 solid 1px;
                margin: 10px auto;
                padding: 10px;
                width: 70vw;
                height: 300px;
            }

            .container__header {
                position: absolute;
                top: 1rem;
                right: 1rem;
            }
        </style>
        <script>
        	function submitHandler() { // 정규식을 통해 특수문자 입력 불가능하도록 필터링
        		keyword = document.querySelector("input[name='keyword']");
        		value = keyword.value;
        		
        		value = value.replaceAll("&", "&amp;");
        		value = value.replaceAll("\"", "&quot;");
        		value = value.replaceAll("<", "&lt;");
        		value = value.replaceAll("'", "&#x27;");
        		value = value.replaceAll(">", "&gt;");
        		value = value.replaceAll("/", "&#x2F;");
        		value = value.replaceAll("(", "&#40;");
        		value = value.replaceAll(")", "&#41;");
        		
        		keyword.value = value;
        	}
        </script>
        <title>Insert title here</title>
    </head>

    <body>
        <h1>Reflected XSS 공격 </h1>

        <div class="form">
            <form action="reflected3.jsp" method="get" onsubmit="return submitHandler()">
                <div class="form__input">
                    <div>
                        <span> 검색 </span>
                        <input type="text" name="keyword" />
                    </div>
                </div>
                <input class="form__button" type="submit" value="검색" />
            </form>
        </div>

        <div class="container">
            <% 
            	request.setCharacterEncoding("UTF-8");
            	String keyword = request.getParameter("keyword"); // 해당 파라미터가 없으면 null값 반환됨
            	if (keyword == null) {
            		keyword = "";
            	}
            %>

                <div class="container__header"> 검색어 <%=keyword%> <!-- keyword 변수에 저장된 값이 출력 -->
                </div>
        </div>

    </body>

    </html>